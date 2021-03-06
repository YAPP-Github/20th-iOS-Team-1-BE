package com.yapp.pet.domain.account;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.account.service.AccountQueryService;
import com.yapp.pet.domain.account.service.AccountService;
import com.yapp.pet.domain.account_image.AccountImageRepository;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.pet.entity.PetSex;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.jwt.TokenType;
import com.yapp.pet.global.util.s3.S3Utils;
import com.yapp.pet.web.account.model.AccountSignUpRequest;
import com.yapp.pet.web.account.model.AccountUpdateRequest;
import com.yapp.pet.web.account.model.AccountValidationResponse;
import com.yapp.pet.web.account.model.MyPageResponse;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yapp.pet.global.TogaetherConstants.*;
import static com.yapp.pet.web.account.model.MyPageResponse.AccountInfoResponse;
import static com.yapp.pet.web.account.model.MyPageResponse.PetInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql({"/data.sql"})
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountQueryService accountQueryService;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountImageRepository accountImageRepository;

    @Autowired
    S3Utils s3Utils;

    @Value("${jwt.token.secret}")
    String secret;

    Account accountWithTokenAndImage;
    Account accountWithTokenWithoutImage;
    Account accountWithoutToken;

    @BeforeEach
    void init(){
        accountWithTokenAndImage = accountRepository.findById(1L).get();
        accountWithTokenWithoutImage = accountRepository.findById(4L).get();

        accountWithoutToken = Account.builder()
                .age(10)
                .sex(AccountSex.MAN)
                .nickname("test")
                .build();
    }

    String createIdToken(String uniqueId){
        long now = (new Date()).getTime();
        Date expiration = new Date(now + 1800000);

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam(JWT_HEADER_PARAM_TYPE, "JWT")
                .setIssuer("yapp")
                .setSubject(uniqueId)
                .setAudience(TokenType.ACCESS.toString())
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .claim(AUTHORITIES_KEY, ROLE)
                .compact();
    }

    AccountUpdateRequest createUpdateRequest() {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setAge(1);
        request.setCity("??????");
        request.setDetail("?????????");
        request.setNickname("new Nick");
        request.setSelfIntroduction("?????? ???????????????~");
        request.setInterestCategories(List.of(Category.DOG_CAFE, Category.WALK));

        return request;
    }

    List<MultipartFile> createMockImageFiles() {
        List<MultipartFile> mockFiles = new ArrayList<>();

        try {
            MockMultipartFile mockMultipartFile1 = new MockMultipartFile(
                    "mockImage1",
                    "cat.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    new FileInputStream(new File("src/test/resources/assets/cat.jpg")));

            MockMultipartFile mockMultipartFile2 = new MockMultipartFile(
                    "mockImage2",
                    "city.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    new FileInputStream(new File("src/test/resources/assets/city.jpg")));

            mockFiles.add(mockMultipartFile1);
            mockFiles.add(mockMultipartFile2);
        } catch (Exception e) {
        }

        return mockFiles;
    }

    @Test
    @DisplayName("???????????? - ??? ????????? ?????? ??? Account ??????")
    void signUp(){
        //when
        SignInResponse signInResponse
                = accountService.signIn(Social.APPLE, "uniqueIdByApple1", null);

        //then
        assertThat(signInResponse.getFirstAccount()).isTrue();
        assertThat(signInResponse.getAccessToken()).isNotNull().isNotEmpty();
        assertThat(signInResponse.getRefreshToken()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("???????????? - ?????? ?????? ?????? (????????? ??????)")
    void signUpAddInfo() {
        //given
        AccountSignUpRequest req = new AccountSignUpRequest();

        req.setAge(25);
        req.setCity("????????? ?????????");
        req.setDetail("???????????? 910");
        req.setNickname("nick");
        req.setSex(AccountSex.MAN);

        //when
        Long accountId = accountService.signUp(accountWithoutToken, req);

        //then
        assertThat(accountId).isEqualTo(accountWithoutToken.getId());
        assertThat(req.getAge()).isEqualTo(accountWithoutToken.getAge());
        assertThat(req.getCity()).isEqualTo(accountWithoutToken.getAddress().getCity());
        assertThat(req.getDetail()).isEqualTo(accountWithoutToken.getAddress().getDetail());
        assertThat(req.getNickname()).isEqualTo(accountWithoutToken.getNickname());
        assertThat(req.getSex()).isEqualTo(accountWithoutToken.getSex());
    }

    @Test
    @DisplayName("???????????? - ?????? ?????? ?????? (????????? ??????)")
    void signUpAddInfoWithImage() {
        //given
        AccountSignUpRequest req = new AccountSignUpRequest();
        req.setAge(25);
        req.setCity("????????? ?????????");
        req.setDetail("???????????? 910");
        req.setNickname("nick");
        req.setSex(AccountSex.MAN);

        List<MultipartFile> imageFiles = createMockImageFiles();
        req.setImageFile(imageFiles.get(0));

        //when
        Long accountId = accountService.signUp(accountWithoutToken, req);

        //then
        assertThat(accountId).isEqualTo(accountWithoutToken.getId());
        assertThat(req.getAge()).isEqualTo(accountWithoutToken.getAge());
        assertThat(req.getCity()).isEqualTo(accountWithoutToken.getAddress().getCity());
        assertThat(req.getDetail()).isEqualTo(accountWithoutToken.getAddress().getDetail());
        assertThat(req.getNickname()).isEqualTo(accountWithoutToken.getNickname());
        assertThat(req.getSex()).isEqualTo(accountWithoutToken.getSex());

        assertThat(accountImageRepository.findById(1L).isEmpty()).isFalse();
    }

    @Test
    @DisplayName("???????????? ??? ??????.")
    void signIn(){
        //given
        Account account = accountRepository.findById(1L).get();
        String refreshToken = account.getToken().getRefreshToken();

        //when
        SignInResponse signInResponse
                = accountService.signIn(Social.APPLE, "unique1", "yapp@email.com");

        //then
        assertThat(signInResponse.getFirstAccount()).isFalse();
        assertThat(signInResponse.getAccessToken()).isNotNull().isNotEmpty();
        assertThat(signInResponse.getRefreshToken()).isNotNull().isNotEmpty();
        assertThat(signInResponse.getRefreshToken()).isNotEqualTo(refreshToken);
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????? - ???????????? ?????? 2??????~10????????? ?????????")
    void successValidateNickname(){
        //given
        String nickname = "?????????";

        //when
        AccountValidationResponse response = accountQueryService.validateNickname(nickname);

        //then
        assertThat(response.isSatisfyLengthCondition()).isTrue();
        assertThat(response.isUnique()).isTrue();
    }

    @Test
    @DisplayName("????????? ?????? - ???????????? ???????????? ??????")
    void duplicateNickname(){
        //given
        String nickname = "????????????";

        //when
        AccountValidationResponse response = accountQueryService.validateNickname(nickname);

        //then
        assertThat(response.isSatisfyLengthCondition()).isTrue();
        assertThat(response.isUnique()).isFalse();
    }

    @Test
    @DisplayName("????????? ?????? - ???????????? ????????? 2??????~10????????? ?????? ??????")
    void notSatisfyLengthConditionNickname(){
        //given
        String nickname = "yapp-togaether";

        //when
        AccountValidationResponse response = accountQueryService.validateNickname(nickname);

        //then
        assertThat(response.isSatisfyLengthCondition()).isFalse();
        assertThat(response.isUnique()).isTrue();
    }

    @Test
    @DisplayName("????????? ?????????????????? ????????? ??? ??????.")
    void getMyPageInfo(){
        //when
        MyPageResponse myPageInfo = accountQueryService.getMyPageInfo(accountWithTokenAndImage, null);

        AccountInfoResponse accountInfo = myPageInfo.getAccountInfo();
        List<PetInfoResponse> petInfos = myPageInfo.getPetInfos();

        //then
        assertThat(myPageInfo.isMyPage()).isTrue();

        assertThat(accountInfo.getNickname()).isEqualTo("????????????");
        assertThat(accountInfo.getAddress()).isEqualTo("??????????????? ?????????");
        assertThat(accountInfo.getAge()).isEqualTo("10???");
        assertThat(accountInfo.getSex()).isEqualTo(AccountSex.MAN);
        assertThat(accountInfo.getSelfIntroduction()).isEqualTo("?????? ????????? ???????????????.");
        assertThat(accountInfo.getInterestCategories()).hasSize(3);
        assertThat(accountInfo.getImageUrl()).isEqualTo("https://togaether.s3.ap-northeast-2.amazonaws.com/account/1655044614407_cat.jpg");

        assertThat(petInfos).hasSize(2);
        PetInfoResponse petInfo = petInfos.get(0);

        assertThat(petInfo.getNickname()).isEqualTo("?????????");
        assertThat(petInfo.getBreed()).isEqualTo("?????????");
        assertThat(petInfo.getAge()).isEqualTo("5");
        assertThat(petInfo.getSex()).isEqualTo(PetSex.MALE);
        assertThat(petInfo.getTags()).hasSize(3);
        assertThat(petInfo.getImageUrl()).isEqualTo("https://acjic.com");
    }

    @Test
    @DisplayName("??? ????????? ?????????????????? ????????? ??? ??????.")
    void getMyPageInfo2(){
        //when
        MyPageResponse myPageInfo = accountQueryService.getMyPageInfo(accountWithTokenWithoutImage, "????????????");

        AccountInfoResponse accountInfo = myPageInfo.getAccountInfo();
        List<PetInfoResponse> petInfos = myPageInfo.getPetInfos();

        //then
        assertThat(myPageInfo.isMyPage()).isFalse();

        assertThat(accountInfo.getNickname()).isEqualTo("????????????");
        assertThat(accountInfo.getAddress()).isEqualTo("??????????????? ?????????");
        assertThat(accountInfo.getAge()).isEqualTo("10???");
        assertThat(accountInfo.getSex()).isEqualTo(AccountSex.MAN);
        assertThat(accountInfo.getSelfIntroduction()).isEqualTo("?????? ????????? ???????????????.");
        assertThat(accountInfo.getInterestCategories()).hasSize(3);
        assertThat(accountInfo.getImageUrl()).isEqualTo("https://togaether.s3.ap-northeast-2.amazonaws.com/account/1655044614407_cat.jpg");
        assertThat(petInfos).hasSize(2);
        PetInfoResponse petInfo = petInfos.get(0);
        assertThat(petInfo.getNickname()).isEqualTo("?????????");
        assertThat(petInfo.getBreed()).isEqualTo("?????????");
        assertThat(petInfo.getAge()).isEqualTo("5");
        assertThat(petInfo.getSex()).isEqualTo(PetSex.MALE);
        assertThat(petInfo.getTags()).hasSize(3);
        assertThat(petInfo.getImageUrl()).isEqualTo("https://acjic.com");
    }

    @Test
    @DisplayName("?????? ????????? ????????? ??? ??????. - ?????? ????????? ?????? ?????? + ?????? ????????? ?????? ??????")
    void updateAccount1(){
        //given
        AccountUpdateRequest request = createUpdateRequest();

        //when
        accountService.updateAccount(accountWithTokenWithoutImage, request);

        //then
        Account findAccount = accountRepository.findById(accountWithTokenWithoutImage.getId()).get();

        assertThat(findAccount.getAge()).isEqualTo(request.getAge());
        assertThat(findAccount.getAddress().getCity()).isEqualTo(request.getCity());
        assertThat(findAccount.getAddress().getDetail()).isEqualTo(request.getDetail());
        assertThat(findAccount.getNickname()).isEqualTo(request.getNickname());
        assertThat(findAccount.getSelfIntroduction()).isEqualTo(request.getSelfIntroduction());
        assertThat(findAccount.getInterestCategories().size()).isEqualTo(request.getInterestCategories().size());
    }

    @Test
    @DisplayName("?????? ????????? ????????? ??? ??????. - ?????? ????????? ?????? ?????? + ?????? ????????? ?????? ??????")
    void updateAccount2() {
        //given
        AccountUpdateRequest request = createUpdateRequest();
        request.setImageFile(createMockImageFiles().get(0));

        //when
        accountService.updateAccount(accountWithTokenWithoutImage, request);

        //then
        Account findAccount = accountRepository.findById(accountWithTokenWithoutImage.getId()).get();

        assertThat(findAccount.getAge()).isEqualTo(request.getAge());
        assertThat(findAccount.getAddress().getCity()).isEqualTo(request.getCity());
        assertThat(findAccount.getAddress().getDetail()).isEqualTo(request.getDetail());
        assertThat(findAccount.getNickname()).isEqualTo(request.getNickname());
        assertThat(findAccount.getSelfIntroduction()).isEqualTo(request.getSelfIntroduction());
        assertThat(findAccount.getInterestCategories().size()).isEqualTo(request.getInterestCategories().size());
        assertThat(findAccount.getAccountImage()).isNotNull();
    }

    @Test
    @DisplayName("?????? ????????? ????????? ??? ??????. - ?????? ????????? ?????? ?????? + ?????? ????????? ?????? ??????")
    void updateAccount3(){
        //given
        AccountUpdateRequest request = createUpdateRequest();

        //when
        accountService.updateAccount(accountWithTokenAndImage, request);

        //then
        Account findAccount = accountRepository.findById(accountWithTokenAndImage.getId()).get();

        assertThat(findAccount.getAge()).isEqualTo(request.getAge());
        assertThat(findAccount.getAddress().getCity()).isEqualTo(request.getCity());
        assertThat(findAccount.getAddress().getDetail()).isEqualTo(request.getDetail());
        assertThat(findAccount.getNickname()).isEqualTo(request.getNickname());
        assertThat(findAccount.getSelfIntroduction()).isEqualTo(request.getSelfIntroduction());
        assertThat(findAccount.getInterestCategories().size()).isEqualTo(request.getInterestCategories().size());
        assertThat(findAccount.getAccountImage()).isNotNull();
    }

    @Test
    @Disabled("?????? data.sql??? ??? ?????? account_image insert ????????? s3??? ?????? ?????? ???????????? ??????????????? ????????? ????????????")
    @DisplayName("?????? ????????? ????????? ??? ??????. - ?????? ????????? ?????? ?????? + ?????? ????????? ?????? ??????")
    void updateAccount4(){
        //given
        AccountUpdateRequest request = createUpdateRequest();
        request.setImageFile(createMockImageFiles().get(0));

        //when
        accountService.updateAccount(accountWithTokenAndImage, request);

        //then
        Account findAccount = accountRepository.findById(accountWithTokenAndImage.getId()).get();

        assertThat(findAccount.getAge()).isEqualTo(request.getAge());
        assertThat(findAccount.getAddress().getCity()).isEqualTo(request.getCity());
        assertThat(findAccount.getAddress().getDetail()).isEqualTo(request.getDetail());
        assertThat(findAccount.getNickname()).isEqualTo(request.getNickname());
        assertThat(findAccount.getSelfIntroduction()).isEqualTo(request.getSelfIntroduction());
        assertThat(findAccount.getInterestCategories().size()).isEqualTo(request.getInterestCategories().size());
        assertThat(findAccount.getAccountImage()).isNotNull();
        assertThat(findAccount.getAccountImage().getOriginName()).isEqualTo("cat.jpg");
    }

}
