package com.cmp.res.service;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cmp.res.entity.User;



@Service
public class PasswordHelper {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    @Value("${password.algorithmName}")
    private String algorithmName = "md5";
    @Value("${password.hashIterations}")
    private int hashIterations = 16;

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public void encryptPassword(User user) {

        user.setSalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }
    public void encryptPassword2(User user) {

        user.setSalt("65f25daeb084ee55310bd406703de664");

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }
    public static void main(String[] args) {
    	PasswordHelper ph=new PasswordHelper();
    	User user=new User();
    	user.setNickName("trsliao");
    	user.setUserName("trsliao");
    	user.setPassword("trsadmin");
    	ph.encryptPassword2(user);
    	System.out.println(user.getSalt()+"   "+user.getPassword());
    	
    	//f8b8e6605f794b971fc5e1bae59ff7c9   dd4a135d264bbc7d93f895d3eb2f2e86
    	//f482e226c44a2e20afccced8fc912195   5c480fc5568dd3ca7c1e288a2f09b126
    	//50d68d8247768e2eae9f2b58f933a4c2
	}
}
