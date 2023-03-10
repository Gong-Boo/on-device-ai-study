## 02.컴퓨터 비전 소개

### 비전을 위한 뉴런 사용하기
``` python
model = Sequential(Dense(units=1))
```
- 이런한 코드를 통해 한개에 레이어에 하나의 뉴런만 있는 신경망을 만들수 있다.
- 더 많은 레이어를 추가 하기 위해서는 밑에 코드를 통해 가능하다.
``` python
model = Sequential( [Dense(units=2), Dense(units=1)]
```
- 하지만 위의 경우, 출력은 뉴런 하나이고 이 뉴런은 하나의 가중치와 하나의 편향만을 학습하게 된다
- 아주 단순한 이미지라 하더라도 하나의 값만으로 표현하기에는 일반적인 이미지에는 너무 많은 내용을 담고 있으므로 뉴런 하나만으로는 이미지를 이해하는 데는 무리가 있다.
- 이미지를 해석하는 법을 배울 때도 컴퓨터가 이해할 수 있는 항목의 개수를 제한하는 것이 좋다.
- 컴퓨터가 이미지를 입력으로 받아서 우리가 원하는 출력 뉴런으로 만드는 방법으로는 원-핫 인코딩이 있다.
- 원-핫 인코딩은 전체 카테고리 수만큼 0을 가지는 벡터를 이용해서 원하는 카테고리만 1로 표현하는 표현법이다.

### 첫 분류기: 의류 구별하기
- 의류 항목을 구별하기 위해서는 어떤 것들이 필요한지 생각해보자.
- 세상에는 다양한 종류의 의류가 있고  규칙 기반 프로그래밍을 사용하면 원하는 결과를 얻기 어려울 것이다.
- 하지만 패션 MNIST라는 데이터셋으로 가능하게 할수있다.

### 데이터: 패션 MNIST
- 알고리즘 학습과 벤치마킹에서 가장 기초적인 데이터셋 중 하나는 MNIST 데이터셋이다.
- 0에서 9까지 손글씨 숫자를 70,000개의 이미지로 담고 있고, 각 이미지는 28 × 28 회색조로 되어 있다.
- 패션 MNIST에는 기존 MNIST에 있는 0에서 9까지의 숫자 이미지 대신, 10가지 다른 종류의 의류 이미지가 들어 있다.

### 패션 MNIST 모델 아키텍처
- 패션 MNIST에 10가지 의류 클래스가 있으므로 우리는 10개 뉴런을 가지는 출력 레이어가 필요하다.
- 우리 이미지는 28 × 28 픽셀 크기(2차원 배열)의 정사각형이므로 이미지를 입력 레이어의 뉴런이 표현하는 방식으로 만들어줘야 하다.
- 이렇게 만드는 과정을 이미지 ‘플래트닝’이라 부른다.
- 학습셋의 6만 개의 이미지가 있다고 한다면 1장에서 말했던 훈련 루프를 돌게 된다.
- 다음으로 6만 개의 레이블링된 이미지에 대한 분류가 이뤄지며, 분류에서의 정확도와 손실값은 옵티마이저가 뉴런값을 조금씩 조정하는 데 사용되고, 이 과정을 계속 반복한다.
- 시간이 지나면 뉴런의 가중치와 편향값이 학습 데이터에 맞게 조정되어 있을 것이다.

### 패션 MNIST 모델 코딩
- 앞에서 설명했던 모델 아키텍처는 굉장히 간단하다.
``` python
model = Sequential(
 [Flatten(input_shape=(28,28)),
 Dense(20, activation=tf.nn.relu),
 Dense(10, activation=tf.nn.softmax)])
```
- Sequentials는 리스트 자료형을 통해 레이어들을 정의할 수 있다.
- 이 코드에서는 하나의 Flatten과 두 개의 Dense가 순서대로 연결된다.
- Flatten(input_shape=(28,28)은 입력 셰입인 28 × 28에서 784 × 1로 플래트닝을 시켜준다.
- activation 파라미터는 활성함수를 정의하는 파라미터로, 레이어 뒤에 붙어서 실행되고 신경망이 복잡한 패턴을 구별하는 데 도움이된다.
- 뉴런 레이어에서 활성함수는 tf.nn.relu이며, ‘rectified linear unit’를 의미한다.
- 이 함수는 입력값이 0보다 작으면 0을 반환하고, 0보다 크면 입력값을 그대로 반환한다.
- 이전 레이어에서 어떤 뉴런이 음수를 반환한다면 다른 뉴런의 양수값을 상쇄시켜버려서 학습된 내용을 무시하게 될 수도있다.
- 이런 경우 출력 레이어는 softmax라는 활성함수를 가지게 된다.

- 다음과 같이 쉽게 패션 MNIST 데이터를 가져올 수 있다.
``` python
data = tf.keras.datasets.fashion_mnist
(training_images, training_labels), (val_images, val_labels) = data.load_data(
```

- 다음 행에서는 배열의 값들을 255로 나눠준다.
- 255로 나누는 이유는 이미지의 픽셀값이 0에서 255로 이루어져 있으므로 255로 나누어서 0에서 1의 사잇값으로 만들어주기 위함이다.
- 이 작업을 정규화 라고 하고 오류가 너무 커져버려서 학습이 되지 않는 문제를 예방하는 데 도움이된다.
- 모델 아키텍처를 정의하고 나면, 이제 손실함수와 옵티마이저를 지정하여 모델을 컴파일해야 한다.
``` java
model.compile(optimizer='adam',
 loss='sparse_categorical_crossentropy',
 metrics=['accuracy'])
```
- 옵티마이저로 adam을 사용했으며 adam은 sgd의 개선된 버전으로 더 좋은 성능을 보인다.
- 또한, metrics=['accuracy'] 파라미터도 사용했으며,  이 파라미터는 학습되는 동안 텐서플로에 정확도를 알려준다.
- 마지막으로 학습 데이터를 넣어 모델을 학습시킨다.
``` python
model.fit(training_images, training_labels, epochs=20)
```
- epochs을 20으로 지정하여 반복 학습 횟수를 20회로 만들었고 학습 이미지와 레이블로 모델을 맞추도록 했다.
- 이 예제를 수행했을 때는, 구글 코랩으로 6만 개의 이미지를 처리하고 학습하는 데 몇 초밖에 걸리지 않았다.

## 컴퓨터 비전을 위한 전이 학습
- 좀 더 큰 이미지, 더 많은 클래스, 색상, 다른 것들이 추가된다면? 이를 위해서는 훨씬 더 복잡한 아키텍처를 만들어야만 한다.
- 예를 들어 MobileNet이라 부르는 아키텍처의 레이어를 표현하였다.
- 대부분이 ‘버틀넥' 이라는 종류의 레이어이다.

## 마치며
- 10가지 패션 종류를 구별하는 모델을 밑바닥부터 만들어 보면서 신경망이 어떻게 구성되는지도 살펴보았다.
- 이미 학습된 파라미터를 다양한 시나리오에서 쉽게 사용할 수 있는 전이 학습에 대한 개념을 소개하였다.
