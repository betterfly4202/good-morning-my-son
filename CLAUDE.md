# CLAUDE.md

## 프로젝트
**Good Morning My Son** - 육아 유튜버 콘텐츠 기반 RAG 엔진

## 핵심 규칙
1. **문서가 진실** - 추측하지 말고, 문서에 근거하라
2. **Plan 먼저** - 코딩 전에 Plan Mode로 계획 확정
3. **검증 필수** - 구현 후 반드시 테스트/검증
4. **모르면 질문** - 불확실하면 멈추고 물어보라

## 기술 스택
- Kotlin + Spring Boot 3.x
- PostgreSQL + pgvector
- OpenAI Embeddings

## 주요 명령어
```bash
./gradlew build          # 빌드
./gradlew test           # 테스트
./gradlew bootRun        # 실행
```

## Slash Commands
- `/implement` - 구현 워크플로우
- `/review` - 코드 리뷰
- `/commit` - 커밋 생성

## 디렉토리 구조
```
src/main/kotlin/com/goodmorning/
├── subtitle/     # 자막 수집/파싱
├── embedding/    # 임베딩 생성
├── vectordb/     # 벡터 저장소
└── rag/          # RAG 파이프라인
```

## 상세 문서
- `.claude/architecture.md` - 전체 아키텍처
- `.claude/agents/` - 서브에이전트 정의
- `.claude/commands/` - Slash command 정의
- `.claude/features/` - Feature별 spec

## 현재 Phase
End-to-End 프로토타입 구축 중
