export interface TermCategory {
  id: string;
  name: string;
  icon: string;
  description: string;
  termCount: number;
}

export interface Term {
  id: string;
  categoryId: string;
  name: string;
  definition: string;
  example: string;
  difficulty: "초급" | "중급" | "고급";
}

export const termCategories: TermCategory[] = [
  {
    id: "technical",
    name: "기술적 분석",
    icon: "LineChart",
    description: "차트와 지표를 이용한 주가 분석 방법",
    termCount: 8,
  },
  {
    id: "fundamental",
    name: "기본적 분석",
    icon: "FileText",
    description: "기업의 재무제표와 가치 평가",
    termCount: 7,
  },
  {
    id: "chart-pattern",
    name: "차트 패턴",
    icon: "TrendingUp",
    description: "반복되는 주가 패턴 인식",
    termCount: 6,
  },
  {
    id: "trading",
    name: "매매 기법",
    icon: "ArrowLeftRight",
    description: "실전 매매에 사용되는 전략과 기법",
    termCount: 6,
  },
  {
    id: "market",
    name: "시장 용어",
    icon: "Building2",
    description: "주식 시장의 기본 구조와 용어",
    termCount: 7,
  },
  {
    id: "derivative",
    name: "파생상품",
    icon: "Layers",
    description: "선물, 옵션 등 파생상품 관련 용어",
    termCount: 5,
  },
];

export const terms: Term[] = [
  // 기술적 분석
  {
    id: "t1",
    categoryId: "technical",
    name: "이동평균선 (MA)",
    definition:
      "일정 기간 동안의 주가 평균을 연결한 선. 5일, 20일, 60일, 120일선이 주로 사용되며 주가의 추세를 파악하는 데 활용됩니다.",
    example:
      "삼성전자의 20일 이동평균선이 60일 이동평균선을 상향 돌파하면 '골든크로스'로 매수 신호로 해석합니다.",
    difficulty: "초급",
  },
  {
    id: "t2",
    categoryId: "technical",
    name: "RSI (상대강도지수)",
    definition:
      "일정 기간 동안 상승폭과 하락폭의 비율로 계산하는 모멘텀 지표. 0~100 범위로, 70 이상이면 과매수, 30 이하면 과매도로 판단합니다.",
    example:
      "RSI가 30 이하로 떨어진 종목은 과매도 상태로, 반등 가능성을 고려해 매수 타이밍을 잡을 수 있습니다.",
    difficulty: "중급",
  },
  {
    id: "t3",
    categoryId: "technical",
    name: "MACD",
    definition:
      "Moving Average Convergence Divergence의 약자. 단기 이동평균과 장기 이동평균의 차이를 나타내는 추세 추종 지표입니다.",
    example:
      "MACD 선이 시그널 선을 상향 돌파하면 '골든크로스'로, 매수 시점으로 해석합니다.",
    difficulty: "중급",
  },
  {
    id: "t4",
    categoryId: "technical",
    name: "볼린저 밴드",
    definition:
      "이동평균선을 중심으로 표준편차를 이용해 상단밴드와 하단밴드를 설정한 지표. 주가의 변동성과 추세를 동시에 분석합니다.",
    example:
      "주가가 하단 밴드를 터치한 후 반등하면 매수, 상단 밴드를 터치한 후 하락하면 매도 신호로 활용합니다.",
    difficulty: "중급",
  },
  {
    id: "t5",
    categoryId: "technical",
    name: "거래량",
    definition:
      "일정 기간 동안 거래된 주식의 수. 주가 움직임의 신뢰도를 확인하는 핵심 보조 지표입니다.",
    example:
      "주가가 상승할 때 거래량도 함께 증가하면 상승 추세의 신뢰도가 높다고 판단합니다.",
    difficulty: "초급",
  },
  {
    id: "t6",
    categoryId: "technical",
    name: "스토캐스틱",
    definition:
      "일정 기간 동안의 최고가와 최저가 범위에서 현재 종가의 위치를 백분율로 나타낸 지표. %K와 %D 두 선으로 구성됩니다.",
    example:
      "%K가 20 이하에서 %D를 상향 돌파하면 과매도 구간에서의 반등 신호로 해석합니다.",
    difficulty: "고급",
  },
  {
    id: "t7",
    categoryId: "technical",
    name: "지지선/저항선",
    definition:
      "지지선은 주가가 하락할 때 더 이상 내려가지 않는 가격대, 저항선은 상승할 때 더 이상 올라가지 않는 가격대입니다.",
    example:
      "삼성전자가 70,000원 부근에서 여러 번 반등했다면 70,000원이 지지선으로 작용하고 있다고 볼 수 있습니다.",
    difficulty: "초급",
  },
  {
    id: "t8",
    categoryId: "technical",
    name: "OBV (On Balance Volume)",
    definition:
      "거래량을 누적하여 주가 추세를 예측하는 지표. 주가 상승일의 거래량은 더하고, 하락일의 거래량은 빼서 계산합니다.",
    example:
      "주가는 횡보하는데 OBV가 상승하면 매집이 이루어지고 있다는 신호로 해석합니다.",
    difficulty: "고급",
  },

  // 기본적 분석
  {
    id: "f1",
    categoryId: "fundamental",
    name: "PER (주가수익비율)",
    definition:
      "주가를 주당순이익(EPS)으로 나눈 값. 기업의 수익 대비 주가가 얼마나 높은지를 나타내며, 낮을수록 저평가 가능성이 있습니다.",
    example:
      "삼성전자의 PER이 12배이고 업종 평균이 15배라면, 상대적으로 저평가되어 있다고 볼 수 있습니다.",
    difficulty: "초급",
  },
  {
    id: "f2",
    categoryId: "fundamental",
    name: "PBR (주가순자산비율)",
    definition:
      "주가를 주당순자산가치(BPS)로 나눈 값. 1 미만이면 장부가치보다 시장가치가 낮은 상태로 저평가 가능성을 시사합니다.",
    example:
      "PBR이 0.8인 기업은 장부가치의 80% 수준에서 거래되고 있어 자산가치 대비 저평가 상태입니다.",
    difficulty: "초급",
  },
  {
    id: "f3",
    categoryId: "fundamental",
    name: "ROE (자기자본이익률)",
    definition:
      "당기순이익을 자기자본으로 나눈 비율. 주주가 투자한 자본으로 얼마나 효율적으로 이익을 창출하는지를 나타냅니다.",
    example:
      "ROE가 15%라면 주주가 투자한 100원으로 15원의 이익을 만들어냈다는 의미입니다.",
    difficulty: "중급",
  },
  {
    id: "f4",
    categoryId: "fundamental",
    name: "EPS (주당순이익)",
    definition:
      "당기순이익을 발행주식 수로 나눈 값. 주식 1주당 벌어들이는 순이익을 의미하며 PER 계산의 기초가 됩니다.",
    example:
      "기업의 당기순이익이 1조원이고 발행주식 수가 1억주라면 EPS는 10,000원입니다.",
    difficulty: "초급",
  },
  {
    id: "f5",
    categoryId: "fundamental",
    name: "배당수익률",
    definition:
      "1주당 배당금을 현재 주가로 나눈 비율. 투자 대비 배당으로 받을 수 있는 수익률을 나타냅니다.",
    example:
      "주가가 50,000원이고 연간 배당금이 1,000원이면 배당수익률은 2%입니다.",
    difficulty: "초급",
  },
  {
    id: "f6",
    categoryId: "fundamental",
    name: "영업이익률",
    definition:
      "영업이익을 매출액으로 나눈 비율. 기업의 본업에서 얼마나 효율적으로 이익을 창출하는지 나타냅니다.",
    example:
      "매출 100억, 영업이익 20억이면 영업이익률 20%로, 제조업 기준 우수한 수준입니다.",
    difficulty: "중급",
  },
  {
    id: "f7",
    categoryId: "fundamental",
    name: "부채비율",
    definition:
      "부채총액을 자기자본으로 나눈 비율. 기업의 재무 안정성을 나타내며 일반적으로 200% 이하가 적정합니다.",
    example:
      "부채비율이 80%라면 자기자본 100원당 부채가 80원인 상태로 재무구조가 안정적입니다.",
    difficulty: "중급",
  },

  // 차트 패턴
  {
    id: "c1",
    categoryId: "chart-pattern",
    name: "골든크로스",
    definition:
      "단기 이동평균선이 장기 이동평균선을 아래에서 위로 돌파하는 현상. 강세 전환 신호로 해석됩니다.",
    example:
      "20일 이동평균선이 60일 이동평균선을 상향 돌파하면 골든크로스가 발생한 것으로, 매수 신호입니다.",
    difficulty: "초급",
  },
  {
    id: "c2",
    categoryId: "chart-pattern",
    name: "데드크로스",
    definition:
      "단기 이동평균선이 장기 이동평균선을 위에서 아래로 하향 돌파하는 현상. 약세 전환 신호로 해석됩니다.",
    example:
      "20일선이 60일선을 하향 돌파하면 데드크로스로, 추가 하락 가능성을 시사합니다.",
    difficulty: "초급",
  },
  {
    id: "c3",
    categoryId: "chart-pattern",
    name: "헤드앤숄더",
    definition:
      "왼쪽 어깨-머리-오른쪽 어깨 형태의 패턴. 상승 추세의 천장에서 나타나며 하락 전환을 예고합니다.",
    example:
      "세 번의 고점 중 가운데가 가장 높은 헤드앤숄더 패턴이 완성되면 넥라인 이탈 시 매도 신호입니다.",
    difficulty: "고급",
  },
  {
    id: "c4",
    categoryId: "chart-pattern",
    name: "더블바텀",
    definition:
      "주가가 비슷한 수준에서 두 번 저점을 형성한 후 상승하는 W자형 패턴. 바닥 확인 후 반등 신호입니다.",
    example:
      "주가가 10,000원에서 두 번 바닥을 찍고 반등하면 더블바텀 패턴으로 매수 타이밍입니다.",
    difficulty: "중급",
  },
  {
    id: "c5",
    categoryId: "chart-pattern",
    name: "삼각수렴",
    definition:
      "고점은 점차 낮아지고 저점은 점차 높아지며 삼각형 형태로 수렴하는 패턴. 방향성 돌파 시 큰 움직임이 예상됩니다.",
    example:
      "삼각수렴 패턴에서 상방 돌파 시 거래량 동반 여부를 확인한 후 매수에 참여합니다.",
    difficulty: "중급",
  },
  {
    id: "c6",
    categoryId: "chart-pattern",
    name: "갭 (Gap)",
    definition:
      "전일 종가와 당일 시가 사이에 가격 공백이 생기는 현상. 상승갭은 호재, 하락갭은 악재를 반영합니다.",
    example:
      "실적 발표 후 5% 상승갭이 발생하면, 갭을 채우지 않고 추가 상승할 가능성이 높습니다.",
    difficulty: "중급",
  },

  // 매매 기법
  {
    id: "tr1",
    categoryId: "trading",
    name: "손절매",
    definition:
      "보유 종목의 손실이 일정 수준에 도달했을 때 추가 손실을 막기 위해 매도하는 것. 리스크 관리의 핵심입니다.",
    example:
      "매수가 대비 -5% 지점에 손절매를 설정하면, 72,000원에 매수한 경우 68,400원에서 자동 매도됩니다.",
    difficulty: "초급",
  },
  {
    id: "tr2",
    categoryId: "trading",
    name: "분할매수",
    definition:
      "투자 금액을 한 번에 투자하지 않고 여러 번에 나누어 매수하는 방법. 평균 매수단가를 낮추고 리스크를 분산합니다.",
    example:
      "1,000만원 투자 시 3번에 나누어 각 330만원씩 매수하면 시장 변동에 유연하게 대응할 수 있습니다.",
    difficulty: "초급",
  },
  {
    id: "tr3",
    categoryId: "trading",
    name: "스윙 트레이딩",
    definition:
      "수일에서 수주 단위로 주가의 파동을 이용해 매매하는 기법. 단기 트레이딩과 장기 투자의 중간 형태입니다.",
    example:
      "지지선에서 매수하고 저항선에서 매도하는 방식으로 5~20% 수익을 목표로 합니다.",
    difficulty: "중급",
  },
  {
    id: "tr4",
    categoryId: "trading",
    name: "물타기",
    definition:
      "보유 중인 종목의 주가가 하락했을 때 추가 매수하여 평균 매수단가를 낮추는 방법. 신중하게 사용해야 합니다.",
    example:
      "10,000원에 100주 매수 후 8,000원에 100주를 추가 매수하면 평균단가가 9,000원으로 낮아집니다.",
    difficulty: "중급",
  },
  {
    id: "tr5",
    categoryId: "trading",
    name: "익절 (이익실현)",
    definition:
      "보유 종목이 목표가에 도달했을 때 수익을 확정짓기 위해 매도하는 것. 욕심을 부리지 않는 것이 핵심입니다.",
    example:
      "목표 수익률 15%에 도달하면 전량 또는 절반을 매도하여 이익을 실현합니다.",
    difficulty: "초급",
  },
  {
    id: "tr6",
    categoryId: "trading",
    name: "포지션 사이징",
    definition:
      "전체 투자금 대비 개별 종목에 투자하는 비율을 결정하는 방법. 리스크 관리의 핵심 요소입니다.",
    example:
      "전체 투자금의 10% 이상을 단일 종목에 투자하지 않는 규칙을 적용합니다.",
    difficulty: "고급",
  },

  // 시장 용어
  {
    id: "m1",
    categoryId: "market",
    name: "시가총액",
    definition:
      "발행주식 수에 현재 주가를 곱한 값. 기업의 시장에서의 전체 가치를 나타냅니다.",
    example:
      "삼성전자의 발행주식이 약 60억주이고 주가가 72,000원이면 시가총액은 약 432조원입니다.",
    difficulty: "초급",
  },
  {
    id: "m2",
    categoryId: "market",
    name: "공매도",
    definition:
      "주식을 빌려서 먼저 매도한 뒤, 주가가 하락하면 싸게 사서 갚는 투자 기법. 주가 하락에 베팅하는 전략입니다.",
    example:
      "A주식을 10,000원에 공매도한 후 8,000원으로 하락하면 2,000원의 차익을 얻습니다.",
    difficulty: "고급",
  },
  {
    id: "m3",
    categoryId: "market",
    name: "서킷브레이커",
    definition:
      "주가가 급락할 때 시장의 패닉을 방지하기 위해 일시적으로 매매를 정지시키는 제도입니다.",
    example:
      "KOSPI가 전일 대비 8% 이상 하락하면 1단계 서킷브레이커가 발동되어 20분간 매매가 중단됩니다.",
    difficulty: "중급",
  },
  {
    id: "m4",
    categoryId: "market",
    name: "ETF",
    definition:
      "Exchange Traded Fund의 약자. 특정 지수나 자산을 추종하는 펀드를 주식처럼 거래할 수 있는 상품입니다.",
    example:
      "KODEX 200 ETF를 매수하면 KOSPI 200 지수에 포함된 200개 종목에 분산 투자하는 효과를 얻습니다.",
    difficulty: "초급",
  },
  {
    id: "m5",
    categoryId: "market",
    name: "IPO (기업공개)",
    definition:
      "비상장 기업이 주식시장에 상장하여 일반 투자자에게 주식을 공개 발행하는 것입니다.",
    example:
      "카카오뱅크가 2021년 IPO를 통해 코스피에 상장하며 공모가 39,000원에 주식을 발행했습니다.",
    difficulty: "초급",
  },
  {
    id: "m6",
    categoryId: "market",
    name: "배당락일",
    definition:
      "배당금을 받을 권리가 사라지는 날. 이 날 이후에 주식을 매수하면 해당 분기의 배당을 받을 수 없습니다.",
    example:
      "12월 결산법인의 배당락일은 보통 12월 마지막 거래일 2영업일 전이며, 이전에 매수해야 배당을 받습니다.",
    difficulty: "중급",
  },
  {
    id: "m7",
    categoryId: "market",
    name: "호가",
    definition:
      "매수자와 매도자가 제시하는 가격. 매수호가는 사려는 가격, 매도호가는 팔려는 가격을 말합니다.",
    example:
      "현재 매수 1호가가 72,000원, 매도 1호가가 72,100원이면 스프레드는 100원입니다.",
    difficulty: "초급",
  },

  // 파생상품
  {
    id: "dv1",
    categoryId: "derivative",
    name: "선물 (Futures)",
    definition:
      "미래의 특정 시점에 특정 가격으로 자산을 사고팔기로 약속하는 계약. 레버리지 효과로 소액으로 큰 금액을 거래할 수 있습니다.",
    example:
      "KOSPI200 선물을 350포인트에 매수하고 355포인트에 매도하면 5포인트 × 25만원 = 125만원의 수익입니다.",
    difficulty: "고급",
  },
  {
    id: "dv2",
    categoryId: "derivative",
    name: "콜옵션",
    definition:
      "특정 자산을 미래에 정해진 가격으로 살 수 있는 권리. 주가 상승을 예상할 때 매수합니다.",
    example:
      "행사가 70,000원의 삼성전자 콜옵션을 프리미엄 2,000원에 매수하고, 주가가 75,000원이 되면 3,000원의 순이익입니다.",
    difficulty: "고급",
  },
  {
    id: "dv3",
    categoryId: "derivative",
    name: "풋옵션",
    definition:
      "특정 자산을 미래에 정해진 가격으로 팔 수 있는 권리. 주가 하락을 예상할 때 매수합니다.",
    example:
      "주가 하락에 대비한 헤지 수단으로 풋옵션을 매수하면 하락 시 손실을 제한할 수 있습니다.",
    difficulty: "고급",
  },
  {
    id: "dv4",
    categoryId: "derivative",
    name: "레버리지",
    definition:
      "적은 자본으로 더 큰 규모의 거래를 하는 것. 수익도 확대되지만 손실도 동일하게 확대됩니다.",
    example:
      "2배 레버리지 ETF는 기초자산이 1% 상승하면 2% 수익, 1% 하락하면 2% 손실이 발생합니다.",
    difficulty: "중급",
  },
  {
    id: "dv5",
    categoryId: "derivative",
    name: "헤지 (Hedge)",
    definition:
      "기존 투자 포지션의 손실 위험을 줄이기 위해 반대 방향의 투자를 하는 것입니다.",
    example:
      "주식을 보유하면서 풋옵션을 매수하면, 주가 하락 시 옵션 수익으로 주식 손실을 상쇄할 수 있습니다.",
    difficulty: "고급",
  },
];
