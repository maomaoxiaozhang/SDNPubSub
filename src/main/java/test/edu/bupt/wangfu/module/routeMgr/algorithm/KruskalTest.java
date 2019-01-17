package test.edu.bupt.wangfu.module.routeMgr.algorithm; 

import edu.bupt.wangfu.module.routeMgr.algorithm.Kruskal;
import edu.bupt.wangfu.module.routeMgr.util.Edge;
import edu.bupt.wangfu.module.routeMgr.util.Node;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.HashSet;
import java.util.Set;

/** 
* Kruskal Tester. 
* 
* @author <Authors name> 
* @since <pre>Ò»ÔÂ 9, 2019</pre> 
* @version 1.0 
*/ 
public class KruskalTest {
    Kruskal test = null;
    Set<Node> select = null;
    Set<Edge> e = null;

    @Before
    public void before() throws Exception {
        test = new Kruskal();
        select = new HashSet<>();
        e = new HashSet<>();
    }

    @After
    public void after() throws Exception {
    }

    /**
    *
    * Method: KRUSKAL(Set<Node> select, Set<Edge> e)
    *
    */
    @Test
    public void testKRUSKAL() throws Exception {
        //TODO: Test goes here...
        Set<Edge> result = new HashSet<>();
        result.add(new Edge());
        Assert.assertEquals(result, test.KRUSKAL(select, e));
    }


} 
