# Optimization-Algorithm

## 모의 담금질 기법
모의 담금질(Simulated Annealing) 기법은 높은 온도에서 액체 상태인 물질이 온도가 점차 낮아지면서 결정체로 변하는 과정을 모방한 해 탐색 알고리즘이다. 용융 상태에서의 물질의 분자가 자유로이 움직이는 것처럼 해를 탐색하는 과정도 특정한 패턴 없이 이루어진다. 그러나 온도가 낮아질수록 움직임이 줄어 결정체가 되는 과정처럼 해 탐색 과정 또한 점점 더 규칙적인 방식으로 이루어진다.  

![Algorithm](https://user-images.githubusercontent.com/80511210/121558366-98854f80-ca50-11eb-93e2-03f9b839430c.jpg)

모의 담금질 기법의 특징은 확률개념을 도입하여 현재 해의 이웃해 중에서 보다 나쁜 해로 이동하는 자유로움을 보인다는 것이다.

해가 될 수 있는 후보해들의 집합 T에 대해서 T가 점차 낮아질수록 나쁜 해로 이동하는 확률 또한 점차 작아진다.

---
### 모의 담금질 알고리즘 구현 (Optimization.java)
입력 : 함수의 최고차항 ```n``` , 각 항의 계수 ```a[i]```,탐색할 범위 ```lower```, ```upper```                      
출력 : 함수의 최적해
```
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
```
f0가 초기해이고, f1은 랜덤하게 정해진 이웃해를 의미한다.
두 값을 비교하며 둘 중 더 우수한 해를 탐색하도록 한다.

한편, t의 범위를 좁혀가며 t가 탐색이 자유로워질 확률 즉, 나쁜 이웃해로 탐색할 확률 p에 영향을 주도록 한다.
여기서 p는 t가 클수록, 두 해 f0와 f1의 거리가 가까울수록(d가 작을수록) 큰 값을 가진다.

위와 같은 방식을 충분히 많이 반복하며 더 이상 우수한 해를 찾지 못할 때 반복을 종료한다.


---
### 회귀식 추정 (Estiminate.java)
이 모의담금질 기법을 이용해서 독립변수에 따른 종속변수 데이터의 관계를 에러를 최소화하여 가장 잘 설명할 수 있는 수학적 모형을 구해보자.

내가 선택한 데이터는 월별 강수량 데이터를 뽑았다. 

![data](https://github.com/akizakic/akizakic.github.io/blob/main/KakaoTalk_20220617_161307889.png?raw=true)

위의 데이터에서 강수량 데이터를 그래프로 나타내보면 아래와 같이 2차 함수 모양이 나타난다.
![datagraph](https://github.com/akizakic/akizakic.github.io/blob/main/KakaoTalk_20220617_160310124.png?raw=true)

        
비선형 모델 ax²+bx+c를 선정하였고, 앞서 구현한 함수의 최적해를 찾는 모의담금질 기법 알고리즘을 수정해 각 데이터 값에 대해 오차가 가장 적게 나타나는 해인 a, b, c를 탐색하였다.

![result](https://github.com/akizakic/akizakic.github.io/blob/main/KakaoTalk_20220617_163004262.png?raw=true)

해를 탐색할 범위를 -100부터 100까지로 입력해주었다.
오차를 최소로 하는 해 a, b, c가 순서대로 출력되었으며, 그 아랫줄은 x가 1부터 12까지일 때, 각 데이터와의 오차를 모두 더한 값이다.

모의담금질 기법 알고리즘을 이용한 회귀식 추정으로 월별 총강수량 비선형 모델 ax²+bx+c를 -5.1x² +54.8x +17으로 선정하였다.

![results](https://github.com/akizakic/akizakic.github.io/blob/main/KakaoTalk_20220617_170814574.png?raw=true)

여러번 실행을 시켜보고 그에 따른 그래프를 그려보았다.                  
최적해로 얻어지는 a, b, c의 값에 대해서도 일정하게 나타나지 않았으며, 기존 데이터 값과 비교했을 때의 오차값 또한 크게 나타났다.                                
이는 해당 알고리즘이 parameter 추정 알고리즘으로서 미흡한 점들이 있다는 것을 확인시켜준다.                             
최적해를 찾는 정확성을 높이기 위해 반복횟수를 늘리거나, 이웃해를 선택하는 방식을 다르게 하는 방법 등을 택할 수 있다. 이처럼 모의담금질 기법 알고리즘의 결과 정확도와 성능은 반복 횟수, 이웃해 선택 방법, 냉각율, 탐색 범위 등을 어떻게 지정하느냐에 따라 크게 달라질 수 있다는 점을 확인하였다.
