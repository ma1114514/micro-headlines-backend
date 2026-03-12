

import com.microheadlines.common.Result;
import com.microheadlines.common.ResultCodeEnum;
import com.microheadlines.util.AESUtil;
import com.microheadlines.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class TestJwtUtil {
    @Test
    public void TestJwtUtilFunction(){
        JwtUtil instance = JwtUtil.getInstance();
        String tokenAccess = instance.generateAccessToken(1L, "test01", "test01");
        System.out.println(tokenAccess);
        System.out.println(instance.getUserIdFromToken(tokenAccess));
        System.out.println(instance.getRolesFromToken(tokenAccess));
        System.out.println(instance.getRemainingMillis(tokenAccess)/(1000*60));
        System.out.println(instance.isAccessToken(""));
        System.out.println(instance.isTokenExpired("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6InRlc3QwNCIsImlzcyI6Im1pY3JvLWhlYWRsaW5lcyIsInR5cGUiOiJhY2Nlc3MiLCJleHAiOjE3NjYwNTQyNjQ0NjEsInVzZXJJZCI6NCwiaWF0IjoxNzY1OTY3ODY0NDYxLCJ1c2VybmFtZSI6InRlc3QwNCJ9.K0H_eeg9CN7wN273T9bdUAbHAX8bnZFj85ayyjXbfck"));
        System.out.println("===============================================");

        System.out.println(AESUtil.encrypt("test04"));
        System.out.println(AESUtil.isEncrypted("plzEdzAY4YQxR4SJZrlKaYO4RlsIa4sII2OqFj5OdZrULg=="));
        System.out.println(AESUtil.decrypt("plzEdzAY4YQxR4SJZrlKaYO4RlsIa4sII2OqFj5OdZrULg=="));
        System.out.println("================================================");


        System.out.println(Result.build(null, ResultCodeEnum.PASSWORD_ERROR).toString());

    }

}
