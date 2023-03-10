## 01.인공지능과 머신러닝 소개
### 인공지능이란?
- 인공지능은 인공적으로 지능을 생성해내는 것이 아니라, 인공적으로 만들어진 지능을 뜻합니다.
- 그렇게 만들어진 인공지능은 인간의 행동을 대략적으로 모방할 수 있습니다.
- 수많은 샘플을 '보여줌으로써' 컴퓨터가 스스로 이미지의 내용을 배우도록 할 수 있습니다.

<img width="400" alt="스크린샷 2023-02-11 오후 4 25 52" src="https://user-images.githubusercontent.com/49600974/218246222-7aa1ec0b-103a-4ab8-b11c-dfa1d7093018.png">

- 듬성듬성 구멍이 난 티셔츠 같은 일반적이지 않은 티셔츠는 여러 가지 예외(수많은 if문)를 만듭니다.
- 수많은 신발과 티셔츠 예시를 구분해서 보여주면, 그는 무엇이 신발을 구성하는지, 무엇이 티셔츠를 구성하는지 바로 이해할 수 있을 것입니다.
- 컴퓨터가 그렇게 할 수 있도록 여러분이 기존에 작성해 오던 코드와는 다른, 새로운 방식으로 코드를 작성해야 합니다.
- 현재 인공지능 기술의 폭발적인 발전은 대량의 데이터 가용성과 함께 개선된 컴퓨터 성능 덕분입니다.

### 머신러닝이란?
- 이런 방법으로 컴퓨터에 프로그래밍하기 때문에 머신러닝이라는 용어를 사용합니다.
- 즉, 머신러닝 코드를 작성한다는 것은 사람이 직접 파라미터를 찾지 않고 컴퓨터가 스스로 파라미터를 찾아가도록 하는 코드를 작성하는 것입니다.

### 전통적인 프로그래밍에서 머신러닝으로 넘어가기
- 이것이 바로 규칙 기반 프로그래밍이라고도 부르는 전통적인 프로그래밍 방식입니다.
- 이러한 시나리오상에서 프로그래머의 핵심 역할은 규칙을 찾아내는 것입니다.

### 컴퓨터가 어떻게 학습할 수 있을까요?
#### 1단계: 정답 추측하기
- 우선, 정답을 모르기 때문에 추측만으로 답을 구합니다.

#### 2단계: 추측한 결과에서 정확도 측정하기
- 이것을 실제 '정답'과 비교하여 추측이 얼마나 좋은지 나쁜지를 도출합니다.
- 여기서 추측값이 정답과 차이 나는 정도를 손실이라고도 부릅니다.

#### 3단계: 추측 최적화하기
- 최적화는 한마디로 파라미터를 조금씩 조정해 가며 에러가 가장 적게 나오도록 만들어가는 방법입니다.
- 이 과정을 반복하여 최적의 W와 B 파라미터 값을 찾습니다.
- 추측값을 만들어내고, 그 값이 좋은지 나쁜지 계산하고, 그 정보를 바탕으로 다음 추측값을 최적화하는 이 과정을 반복하면 컴퓨터는 시간이 지나면서 W와 B 파라미터(혹은 다른 무엇이든)를 '학습'해 갑니다.

### 머신러닝 코드 구현하기
- 준비된 데이터를 기반으로 스스로 W와 B를 찾아가기 때문에 학습이 끝나고 모델에 x값을 넣으면 적절한 y값을 반환합니다.

<img width="327" alt="스크린샷 2023-02-11 오후 4 33 09" src="https://user-images.githubusercontent.com/49600974/218246460-e737335f-5b2c-4959-9181-21e72bcd4333.png">

- Dense는 왼편의 노드(뉴런)를 오른편의 모든 노드(뉴런)와 연결하는 방식입니다.
- 이 노드들은 '레이어'('층'이라고도 합니다)의 행렬을 이룹니다.
- 그 외에 다른 레이어가 정의되지는 않았으므로 Sequential은 한 레이어만 가집니다.

<img width="496" alt="스크린샷 2023-02-11 오후 4 34 46" src="https://user-images.githubusercontent.com/49600974/218246509-b8bbd90d-107f-40ea-89a9-4054bf17f18d.png">

- 확률적 경사 하강법은 손실을 최소화하기 위해 사용하는 최적화이며, 평균 제곱 오차 손실함수로 계산하는 데 사용합니다.

<img width="498" alt="스크린샷 2023-02-11 오후 4 35 20" src="https://user-images.githubusercontent.com/49600974/218246531-888def6d-d541-4ab9-bc3f-936fa9e37aed.png">

- 텐서플로에서는 보통 이 과정을 피팅이라 부릅니다.

<img width="501" alt="스크린샷 2023-02-11 오후 4 35 40" src="https://user-images.githubusercontent.com/49600974/218246541-7693e679-4822-4ed0-ab14-3205074b5932.png">

- 손실이 줄어들수록 모델의 성능이 나아져서 점점 정답에 가까운 값을 반환하게 된다는 점을 기억해주세요.
- 이 손실은 뉴런이 찾은 W와 B값이 정답 W와 B값과 아주 조금만 차이가 난다는 의미입니다.
- 첫 번째는 신경망이 확실성이 아닌 가능성을 다루기 때문에 완벽하게 정확하다고 할 수는 없습니다.
- 두 번째, 신경망에 넣은 데이터의 수가 작기 때문입니다.
- 이처럼 머신러닝은 확률을 다룹니다.
- 여기서 예측(혹은 추론)은 모델이 학습한 것들을 바탕으로 출력값을 알아내고 하겠지만 이 출력값은 완벽하게 정확하진 않습니다.

### 전통적인 프로그래밍과 머신러닝의 차이점
- 여러분(프로그래머)이 규칙을 알아내지 않는다는 점입니다.
- 대신 데이터와 정답을 주입하여 컴퓨터가 스스로 규칙을 알아내도록 합니다.

<img width="478" alt="스크린샷 2023-02-11 오후 4 38 04" src="https://user-images.githubusercontent.com/49600974/218246625-6118edec-e74b-4b3b-a691-4b6852ff7873.png">

- 예시 중 하나는 컴퓨터 비전 분야입니다.

### 모바일 모델 제작하기
- 예를 들어, 이미 만들어져 있는 모델과 턴키 솔루션을 사용하여 문제를 해결할 수 있고, 또 다른 방법으로 전이 학습을 많이 사용합니다.

### 기타
- https://github.com/tucan9389/ondevice-ml-book/blob/main/BookSource/Chapter01/firstmodel.py
