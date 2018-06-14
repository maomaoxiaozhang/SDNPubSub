package module.util;

import module.manager.ldap.TopicEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 主题树编码，从数据库取出记录格式为TopicEntity，编码成ipv6的格式（分配100位）
 * 层序遍历主题树，为每一层分配log2N位，剩余补0
 *
 * @author caoming
 */
public class Encode {
    //主题树层次对应的编码
    private Map<Integer, Map<String, String>> map4tree;

    public Map encodePerLevel(List<TopicEntry> list) {
        Map<String, String> map4level = new HashMap<>();
        int n = cal(list.size());
        for (int i = 0; i < list.size(); i++) {
            String code = num2code(i, n);
            map4level.put(list.get(i).getTopicName(), code);
        }
        return map4level;
    }

    /**
     * 计算需要的编码位数，换底公式：log2N = logN/log2
     *
     * @param n
     *         主题树的个数
     *
     * @return 对应的编码位数
     */
    public int cal(int n) {
        double result = Math.log(n)/Math.log(2);
        return (int) (result == Math.ceil(result) ? result+1 : Math.ceil(result));
    }

    /**
     * 将数字转换为二进制编码格式
     *
     * @param num
     *         需要转换的数字
     *
     * @return 对应的二进制编码
     */
    public String num2code(int num, int n) {
        String result = Integer.toBinaryString(num);
        String rest = "";
        for (int i = 0; i < n-result.length(); i++)
            rest += "0";
        return rest + result;
    }

    public static void main(String[] args) {
        while (true) {
            Encode encode = new Encode();
            int i = new Scanner(System.in).nextInt();
//            int count = encode.cal(i);
//            System.out.println(encode.num2code(i, count));
            for (int m = 0; m <= i; m++)
                System.out.println(encode.num2code(m, encode.cal(i)));
        }
    }
}
