# Claude Code 프롬프트 관리 가이드

> Boris Cherny 및 Anthropic 공식 Best Practices 기반

---

## 핵심 철학

- **"You don't trust; you instrument"** - 신뢰가 아닌 검증 시스템 구축
- **"Cost per reliable change"** 최적화 - 속도보다 신뢰성 우선
- **반복 워크플로우 코드화** - 자주 하는 작업은 command로 정의

---

## 디렉토리 구조 (권장)

```
project/
├── CLAUDE.md              # 프로젝트 규칙 (git 관리)
├── CLAUDE.local.md        # 개인 설정 (.gitignore)
└── .claude/
    ├── commands/          # 슬래시 커맨드 (/command-name)
    │   ├── commit.md
    │   ├── review.md
    │   └── fix-issue.md
    ├── settings.json      # 권한 설정 (팀 공유)
    └── docs/              # 참고 문서
```

### 주요 파일 역할

| 파일 | 역할 | Git 관리 |
|------|------|----------|
| `CLAUDE.md` | 프로젝트 규칙, 스타일 가이드 | O |
| `CLAUDE.local.md` | 개인 환경 설정 | X |
| `.claude/commands/*.md` | 반복 워크플로우 자동화 | O |
| `.claude/settings.json` | 권한 및 MCP 설정 | O |

---

## CLAUDE.md 작성법

### 포함해야 할 내용
- 일반적인 bash 명령어와 설명
- 핵심 파일 및 유틸리티 함수
- 코드 스타일 가이드라인
- 테스트 실행 방법
- 저장소 관례 (브랜치 명명, 병합 전략)
- 개발 환경 설정
- **실수 기록** - Claude가 잘못한 것을 기록해서 반복 방지

### 작성 원칙
1. **짧고 집중적으로** - 토큰 소비 최적화
2. **자주 업데이트** - PR마다 필요시 수정
3. **팀 공유** - 모든 팀원이 기여

### 예시 구조
```markdown
# 프로젝트명

## 빌드 & 테스트
- `./gradlew build` - 빌드
- `./gradlew test` - 테스트

## 코드 스타일
- Kotlin 공식 스타일 가이드 준수
- 함수는 20줄 이하로 유지

## 금지사항
- prod 환경 직접 접근 금지
- 테스트 없이 코드 커밋 금지

## 실수 기록
- [2025-01-10] XXX 패턴 사용 시 YYY 문제 발생 → ZZZ로 해결
```

---

## Commands 작성법

### 저장 위치
`.claude/commands/{command-name}.md`

### 기본 구조
```markdown
# /{command-name} - 간단한 설명

상세 설명

## 입력
- 필요한 파라미터 설명

## 프로세스
1. 단계 1
2. 단계 2
3. 단계 3

## 규칙
- 규칙 1
- 규칙 2

## 출력 형식
(예시 출력)
```

### $ARGUMENTS 활용
```markdown
GitHub 이슈 $ARGUMENTS 를 분석하고 수정해주세요.
```
→ `/fix-issue 123` 실행 시 "123"이 $ARGUMENTS로 치환

### Boris의 핵심 Command 예시

**`/commit-push-pr`** (하루 수십 번 실행)
```markdown
# 변경사항 커밋, 푸시, PR 생성

1. git status로 변경사항 확인
2. 의미있는 커밋 메시지 작성
3. 푸시 및 PR 생성
```

---

## Subagents 패턴

### Boris가 사용하는 Subagents
| Agent | 역할 |
|-------|------|
| `code-simplifier` | 작업 완료 후 코드 단순화 |
| `verify-app` | 엔드-투-엔드 테스트 |
| `build-validator` | 빌드 검증 |
| `code-architect` | 아키텍처 설계 |

### 권장 구조
```
.claude/
└── commands/
    ├── simplify.md      # code-simplifier 역할
    ├── verify.md        # verify-app 역할
    └── review.md        # reviewer 역할
```

> **참고**: Boris 패턴에서 "agents"는 별도 디렉토리가 아닌 **commands 내에서 역할을 정의**하는 방식입니다.

---

## 워크플로우 패턴

### 1. Plan → Execute 패턴 (기본)
```
[Plan Mode] → 계획 수립 → 승인 → [Auto-accept] → 실행
```
- Shift+Tab 2번으로 Plan Mode 진입
- 계획이 마음에 들 때까지 반복
- 승인 후 자동 실행

### 2. TDD 패턴
```
테스트 작성 → 실패 확인 → 구현 → 테스트 통과 → 커밋
```

### 3. 탐색 → 계획 → 코딩 → 커밋
```
파일 읽기(코드 작성 금지) → "think hard" 계획 → 구현 → 커밋/PR
```

### 4. 시각적 반복 패턴 (UI 작업)
```
목업 제공 → 구현 → 스크린샷 비교 → 2-3회 반복
```

---

## 병렬 세션 관리

Boris의 방식:
- 터미널 5개 (탭 1-5, OS 알림으로 상태 확인)
- 브라우저 5-10개
- 모바일 세션 (아침에 시작, 나중에 확인)

### 다중 Claude 활용
- Claude A: 코드 작성
- Claude B: 코드 리뷰
- Git worktrees로 병렬 작업: `git worktree add ../project-feature-a feature-a`

---

## 검증 시스템 (가장 중요)

> "Probably the most important thing to get great results out of Claude Code: give Claude a way to verify its work."

### 검증 방법
1. **테스트 실행** - 구현 후 반드시 테스트
2. **브라우저 검증** - UI 변경 시 스크린샷 확인
3. **PostToolUse Hook** - 코드 포맷팅 자동화로 CI 실패 방지

### 피드백 루프
검증 → 문제 발견 → 수정 → 재검증 (2-3회 반복으로 품질 2-3배 향상)

---

## 권한 관리

### `/permissions` 설정
- 안전한 작업 사전 허용
- 팀 전체 공유 가능
- "Dangerously Skip Permissions" 회피

### `.claude/settings.json` 예시
```json
{
  "permissions": {
    "allow": [
      "Bash(npm run build)",
      "Bash(npm test)",
      "Read(**/*)"
    ]
  }
}
```

---

## 실수 기록 & 학습

### PR 리뷰 시 학습
1. PR에서 `@.claude` 태그
2. 학습사항을 CLAUDE.md에 추가
3. GitHub Action으로 자동화 가능

### 실수 기록 형식
```markdown
## 실수 기록
- [날짜] 상황 설명 → 해결 방법
```

---

## 모델 선택

Boris의 선택: **Opus 4.5 with thinking**
- 속도보다 신뢰성
- 더 적은 steering 필요
- 더 나은 tool use
- 결과적으로 더 빠른 완료

---

## Sources

- [Anthropic - Claude Code Best Practices](https://www.anthropic.com/engineering/claude-code-best-practices)
- [How Boris Cherny Uses Claude Code](https://karozieminski.substack.com/p/boris-cherny-claude-code-workflow)
- [How the Creator of Claude Code Uses Claude Code](https://paddo.dev/blog/how-boris-uses-claude-code/)
- [VentureBeat - Claude Code Creator Workflow](https://venturebeat.com/technology/the-creator-of-claude-code-just-revealed-his-workflow-and-developers-are)
