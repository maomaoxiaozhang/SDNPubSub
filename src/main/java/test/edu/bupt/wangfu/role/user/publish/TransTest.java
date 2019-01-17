package test.edu.bupt.wangfu.role.user.publish; 

import edu.bupt.wangfu.role.user.publish.Trans;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* Trans Tester. 
* 
* @author <Authors name> 
* @since <pre>һ�� 12, 2019</pre> 
* @version 1.0 
*/ 
public class TransTest { 

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
    *
    * Method: sendMethod(String msg)
    *
    */
    @Test
    public void testSendMethod() throws Exception {
    //TODO: Test goes here...
    }

    /**
    *
    * Method: sendTest(int num)
    *
    */
    @Test
    public void testSendTest() throws Exception {
        //TODO: Test goes here...
        Trans trans = new Trans();
        trans.sendTest(3);
    }


} 
