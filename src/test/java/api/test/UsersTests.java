package api.test;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.userEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UsersTests {
	
	Faker faker; // to get random data we need to create a faker variable
	User userPayload; // pojo class varaiable should create
	public Logger logger;
	
	@BeforeClass
	public void setupData()
	{
		faker = new Faker(); ///faker object has create.
		userPayload=new User();
		/*You never assign a value to your Faker faker instance variable. You do create a Faker faker = new Faker(); in the setupData() method, but that one only exists within that method. If you want to assign that value to your instance variable, you need to remove the initial Faker (so just faker = new Faker();). (Just like you are doing for userPayload.)*/
		 
		userPayload.setId(faker.idNumber().hashCode()); // hash code will generate random id number instead of same id number
		//here generating id number and passing it into userpayload
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPhone(faker.phoneNumber().cellPhone());
	
	//logs
		logger=LogManager.getLogger(this.getClass());
	}
    @Test(priority=1)
	public void testPostUser() 
    {
       Response response=userEndPoints.createUser(userPayload);
       response.then().log().all();
       Assert.assertEquals(response.getStatusCode(),200);
    }
    
    @Test(priority=2)
	public void testGetUserbyName() 
    {
       Response response=userEndPoints.readUser(this.userPayload.getUsername());
       response.then().log().all();
       Assert.assertEquals(response.getStatusCode(),200);
    // Here you can add data validation,json path,schema validation
    }
    
    @Test(priority=3)
    public void testUpdateUserByName()
    {
    	// update a data by using payload
    	userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
    	
    	
    	 Response response=userEndPoints.updateUser(this.userPayload.getUsername(),userPayload);
         response.then().log().body();
         Assert.assertEquals(response.getStatusCode(),200);
         
         //checking data after update
         Response responseAfterUpdate=userEndPoints.readUser(this.userPayload.getUsername());
         Assert.assertEquals(responseAfterUpdate.getStatusCode(),200);
    }
    
    @Test(priority=4)
    public void testDeleteUserByName()
    {
    	 Response response=userEndPoints.deleteUser(this.userPayload.getUsername());
    	 Assert.assertEquals(response.getStatusCode(),200);
    }
}
