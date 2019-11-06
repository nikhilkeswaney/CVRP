import java.util.HashMap;

public class test {

    public static int solution(String S){
        char[] sChar = S.toCharArray();
        int sum = 0;
        for (int i = 0; i < S.length();){
            char current = sChar[i];
            int count = 1;
            while (++i != S.length() && current == sChar[i]){
                count++;
            }
            for (int j = 1; j <= count; j++) {
                sum += count + 1 - j;
            }
        }
        return sum;
    }
    public static void main(String[] args) {
        String a = "abc";
        String b = "c";
        String c = "ab" + b;

        System.out.println(solution("zzzyz"));
    }
}
