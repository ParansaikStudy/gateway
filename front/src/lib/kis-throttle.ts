/**
 * KIS API 서버사이드 스로틀러.
 * 모든 KIS API 호출이 이 큐를 통과하여 최소 간격(MIN_GAP_MS)을 보장합니다.
 * 한국투자증권 모의투자 환경의 초당 호출 제한을 준수합니다.
 */

const MIN_GAP_MS = 500; // 호출 간 최소 500ms 간격

let lastCallTime = 0;
let queue: Promise<void> = Promise.resolve();

/** KIS API 호출 전 대기. 이전 호출로부터 최소 MIN_GAP_MS가 지나야 진행 */
export function kisThrottle(): Promise<void> {
  queue = queue.then(
    () =>
      new Promise<void>((resolve) => {
        const now = Date.now();
        const elapsed = now - lastCallTime;
        const wait = elapsed < MIN_GAP_MS ? MIN_GAP_MS - elapsed : 0;
        setTimeout(() => {
          lastCallTime = Date.now();
          resolve();
        }, wait);
      }),
  );
  return queue;
}

/** 딜레이 유틸 */
export function sleep(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, ms));
}
