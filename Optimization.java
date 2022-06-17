import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Optimization {
    private int niter;
    public ArrayList<Double> hist;

    public Optimization(int niter) {
        this.niter = niter;
        hist = new ArrayList<>();
    }

    public interface Problem {
        double fit(double x);
        boolean isNeighborBetter(double f0, double f1);
    }

    public double solve(Problem p, double t, double a, double lower, double upper) {
        Random r = new Random();
        double x0 = r.nextDouble() * (upper - lower) + lower;
        return solve(p, t, a, x0, lower, upper);
    }

    public double solve(Problem p, double t, double a, double x0, double lower, double upper) {
        Random r = new Random();
        double f0 = p.fit(x0);
        hist.add(f0);

        for (int i=0; i<niter; i++) {
            int kt = (int) t;
            for(int j=0; j<kt; j++) {
                double x1 = r.nextDouble() * (upper - lower) + lower;    //범위 내에서 랜덤한 수 x1
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
        Optimization sa = new Optimization(10);

        int n;
        n = scanner.nextInt();     //최고차항

        double[] coe = new double[n+1];
        for(int i=0; i<n+1; i++) {
            coe[i] = scanner.nextDouble();     //각 항의 계수
        }

        Problem p = new Problem() {
            @Override
            public double fit(double x) {
                double sum = 0;
                for(int i=0; i<n+1; i++) {
                    double tmp = 1;
                    for(int j=n-i; j>0; j--) {
                        tmp *= x;
                    }
                    tmp *= coe[i];
                    sum += tmp;
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

        double x = sa.solve(p, 100, 0.99, lower, upper);
        System.out.println(x);
        System.out.println(p.fit(x));
        System.out.println(sa.hist);
        }
    }





