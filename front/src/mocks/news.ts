export interface NewsItem {
  id: string;
  title: string;
  summary: string;
  source: string;
  time: string;
  category: string;
  imageUrl?: string;
}

export interface NewsSection {
  title: string;
  items: NewsItem[];
}

export const domesticNews: NewsSection[] = [
  {
    title: "코스피 마감",
    items: [
      {
        id: "d1",
        title: "코스피, 외국인·기관 동반 매수에 2,680선 회복",
        summary:
          "외국인이 3거래일 연속 순매수에 나서며 코스피가 2,680선을 회복했다. 반도체·자동차주가 상승을 주도했다.",
        source: "한국경제",
        time: "16:32",
        category: "시장",
      },
      {
        id: "d2",
        title: "코스닥, 바이오주 강세에 870선 공방",
        summary:
          "코스닥은 바이오·게임주 강세에 힘입어 870선에서 공방을 이어갔다.",
        source: "매일경제",
        time: "16:28",
        category: "시장",
      },
    ],
  },
  {
    title: "특징주",
    items: [
      {
        id: "d3",
        title: "한미반도체, HBM 장비 수주 기대감에 10% 급등",
        summary:
          "HBM 후공정 장비 수주 확대 기대감으로 한미반도체가 장중 10% 넘게 급등했다.",
        source: "서울경제",
        time: "14:22",
        category: "종목",
      },
      {
        id: "d4",
        title: "에코프로비엠, 배터리 소재 공급 계약 체결에 6% 상승",
        summary:
          "글로벌 완성차 업체와의 장기 공급 계약 체결 소식에 에코프로비엠이 강세를 보였다.",
        source: "조선비즈",
        time: "13:45",
        category: "종목",
      },
      {
        id: "d5",
        title: "카카오, AI 챗봇 서비스 출시 앞두고 관심 집중",
        summary:
          "카카오가 내달 자체 개발 AI 챗봇 서비스를 출시할 계획이라고 밝히면서 주가가 상승세를 보였다.",
        source: "디지털타임스",
        time: "11:30",
        category: "종목",
      },
    ],
  },
  {
    title: "외국인·기관 수급",
    items: [
      {
        id: "d6",
        title: "외국인, 삼성전자·SK하이닉스 집중 매수…반도체 베팅",
        summary:
          "외국인이 삼성전자 1,200억원, SK하이닉스 800억원 규모를 순매수하며 반도체 업종에 집중 투자했다.",
        source: "한국경제",
        time: "15:50",
        category: "수급",
      },
      {
        id: "d7",
        title: "기관, 2차전지 관련주 순매수 전환",
        summary:
          "기관투자자가 이틀 연속 2차전지 관련주를 순매수하며 업종 분위기 전환에 기여했다.",
        source: "매일경제",
        time: "15:35",
        category: "수급",
      },
    ],
  },
];

export const usNews: NewsSection[] = [
  {
    title: "미국 시장 동향",
    items: [
      {
        id: "u1",
        title: "나스닥, AI 테마 랠리에 사상 최고치 경신",
        summary:
          "엔비디아, 마이크로소프트 등 AI 관련주의 강세로 나스닥 지수가 사상 최고치를 경신했다.",
        source: "Bloomberg",
        time: "07:30",
        category: "시장",
      },
      {
        id: "u2",
        title: "S&P 500, 6,000선 돌파 후 안착 시도",
        summary:
          "연준의 금리 인하 기대감과 기업 실적 호조로 S&P 500 지수가 6,000선을 돌파했다.",
        source: "Reuters",
        time: "07:15",
        category: "시장",
      },
    ],
  },
  {
    title: "빅테크 소식",
    items: [
      {
        id: "u3",
        title: "엔비디아, 차세대 AI 칩 'B200' 양산 시작…실적 기대감 ↑",
        summary:
          "엔비디아가 차세대 AI 칩 B200의 양산을 시작했다고 밝히며 4분기 실적 기대감이 높아졌다.",
        source: "CNBC",
        time: "06:45",
        category: "종목",
      },
      {
        id: "u4",
        title: "애플, AI 아이폰 출시 앞두고 공급망 확대",
        summary:
          "애플이 온디바이스 AI 기능을 탑재한 차기 아이폰 출시를 앞두고 공급망을 대폭 확대하고 있다.",
        source: "WSJ",
        time: "05:20",
        category: "종목",
      },
      {
        id: "u5",
        title: "테슬라, 자율주행 택시 서비스 시범 운영 발표",
        summary:
          "테슬라가 올해 하반기 미국 3개 도시에서 자율주행 택시 서비스를 시범 운영한다고 발표했다.",
        source: "TechCrunch",
        time: "04:50",
        category: "종목",
      },
    ],
  },
  {
    title: "매크로 경제",
    items: [
      {
        id: "u6",
        title: "연준 의사록, 금리 인하 시기 놓고 의견 분분",
        summary:
          "연준 1월 의사록에 따르면 위원들은 금리 인하 시기에 대해 신중한 입장을 유지한 것으로 나타났다.",
        source: "Fed Watch",
        time: "03:00",
        category: "경제",
      },
      {
        id: "u7",
        title: "미국 소비자물가 2.8% 상승…예상 소폭 하회",
        summary:
          "미국 1월 소비자물가지수(CPI)가 전년 대비 2.8% 상승하며 시장 예상(2.9%)을 소폭 하회했다.",
        source: "Bloomberg",
        time: "22:30",
        category: "경제",
      },
    ],
  },
];
