# Stock Front - 기술 스택 정리

> 최종 업데이트: 2026-02-24

## Framework

| 패키지 | 버전 | 용도 |
|---|---|---|
| next | 16.1.6 | App Router 기반 프레임워크 |
| react | 19.2.3 | UI 라이브러리 |
| react-dom | 19.2.3 | React DOM 렌더링 |
| typescript | 5.9.3 | 타입 시스템 |

## 스타일링

| 패키지 | 버전 | 용도 |
|---|---|---|
| tailwindcss | 4.2.1 | 유틸리티 CSS |
| @tailwindcss/postcss | 4.2.1 | Tailwind PostCSS 플러그인 |
| tw-animate-css | 1.4.0 | Tailwind 애니메이션 |
| tailwind-merge | 3.5.0 | Tailwind 클래스 병합 (cn 유틸) |

## UI 컴포넌트

| 패키지 | 버전 | 용도 |
|---|---|---|
| shadcn | 3.8.5 | shadcn/ui CLI |
| radix-ui | 1.4.3 | 헤드리스 UI 프리미티브 |
| class-variance-authority | 0.7.1 | 컴포넌트 variant 관리 |
| clsx | 2.1.1 | 조건부 클래스 결합 |
| lucide-react | 0.575.0 | 아이콘 라이브러리 |

## 상태관리 & 데이터 페칭

| 패키지 | 버전 | 용도 |
|---|---|---|
| @tanstack/react-query | 5.90.21 | 서버 상태 관리 & 캐싱 |

## 폼 & 유효성 검사

| 패키지 | 버전 | 용도 |
|---|---|---|
| react-hook-form | 7.71.2 | 폼 상태 관리 |
| @hookform/resolvers | 5.2.2 | zod 연동 리졸버 |
| zod | 4.3.6 | 스키마 기반 유효성 검사 |

## API 타입 자동생성

| 패키지 | 버전 | 용도 |
|---|---|---|
| openapi-typescript | 7.13.0 | OpenAPI → TypeScript 타입 생성 (dev) |
| openapi-fetch | 0.17.0 | 타입 세이프 API 클라이언트 |

## 개발 도구 (devDependencies)

| 패키지 | 버전 | 용도 |
|---|---|---|
| eslint | 9.39.3 | 린터 |
| eslint-config-next | 16.1.6 | Next.js ESLint 설정 |
| prettier | 3.8.1 | 코드 포맷터 |
| prettier-plugin-tailwindcss | 0.7.2 | Tailwind 클래스 자동 정렬 |
| @types/node | 20.19.33 | Node.js 타입 |
| @types/react | 19.2.14 | React 타입 |
| @types/react-dom | 19.2.3 | React DOM 타입 |

## VS Code 확장 프로그램

| 확장 프로그램 ID | 용도 |
|---|---|
| dbaeumer.vscode-eslint | 린트 |
| esbenp.prettier-vscode | 포맷팅 |
| bradlc.vscode-tailwindcss | Tailwind 자동완성 |
| csstools.postcss | PostCSS 문법 지원 |
| yoavbls.pretty-ts-errors | TS 에러 가독성 |
| formulahendry.auto-rename-tag | 태그 자동 변경 |
| usernamehw.errorlens | 인라인 에러 표시 |

## 주요 스크립트

```bash
npm run dev              # 개발 서버 (Turbopack)
npm run build            # 프로덕션 빌드
npm run start            # 프로덕션 서버
npm run lint             # ESLint 실행
npm run generate-api     # OpenAPI → 타입 자동생성
```
