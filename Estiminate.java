import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Estiminate {
    private int niter;
    public ArrayList<Double> hist;

    public Estiminate(int niter) {
        this.niter = niter;
        hist = new ArrayList<>();
    }

    public interface Problem {
        double fit(double[] x);
        boolean isNeighborBetter(double f0, double f1);
    }

    public double[] solve(Problem p, double t, double a, double lower, double upper) {
        Random r = new Random();
        double[] x0 = {r.nextDouble() * (upper - lower) + lower, r.nextDouble() * (upper - lower) + lower, r.nextDouble() * (upper - lower) + lower};   //범위 내에서 랜덤한 수 x0
        return solve(p, t, a, x0, lower, upper);
    }

    public double[] solve(Problem p, double t, double a, double[] x0, double lower, double upper) {
        Random r = new Random();
        double f0 = p.fit(x0);
        hist.add(f0);

        for (int i=0; i<niter; i++) {
            int kt = (int) t;
            for(int j=0; j<kt; j++) {
                double[] x1 = {r.nextDouble() * (upper - lower) + lower, r.nextDouble() * (upper - lower) + lower, r.nextDouble() * (upper - lower) + lower};    //범위 내에서 랜덤한 수 x1
                double f1 = p.fit(x1);

                if(p.isNeighborBetter(f0, f1)) {    //f0이 f1 보다 우수해일 경우
                    x0 = x1;
                    f0 = f1;
                    hist.add(f0);
                } else {      //f1이 f0 보다 우수해일 경우
                    double d = Math.sqrt(Math.abs(f1 - f0));
                    double p0 = Math.exp(-d/t);      //탐색이 자유로워질 확률 p
                    if(r.nextDouble() < p0 * 0.001) {     //0~1의 수가 확률 p내에 들 경우 (임의로 0.001 곱해줌)
                        x0 = x1;
                        f0 = f1;
                        hist.add(f0);     //나쁜 이웃해로 탐색
                    }
                }
            }
            t *= a;
        }
        return x0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Estiminate sa = new Estiminate(10);

        double[] tem = {6.7,0.8,47.4,157,8.2,91.9,448.9,464.9,212,99.3,67.8,41.4}; //총 강수량 데이터


        Problem p = new Problem() {
            @Override
            public double fit(double[] x) {
                double sum = 0;
                for(int i=0; i<12; i++) {
                    double fun;
                    fun = (x[0] * i * i) + (x[1] * i) + x[2];   //ax^2+bx+c
                    double result;
                    result = tem[i] - fun;     //오차 계산
                    if(result < 0)
                        result *= -1;
                    sum += result;
                }
                return sum;
            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                return f0 > f1;    //최대값 or 최소값
            }
        };

        //탐색 범위 사용자로부터 입력
        int lower = scanner.nextInt();
        int upper = scanner.nextInt();

        double[] x = sa.solve(p, 50000, 0.99, lower, upper);
        System.out.println(x[0] + " " + x[1] + " " + x[2]);
        System.out.println(p.fit(x));
        System.out.println(sa.hist);
    }
}





