# Coding Guidelines

## 1. Precedence

1. **Project guidelines come first, always.** The rules maintained in this repository take utmost priority over everything in Part B.
2. Part B contains general guidelines. They apply only where they do not conflict with a project guideline.

---

# Part A: Project guidelines (highest priority)

These are the source of truth. Do not duplicate their content here; read them.

| Source | What it governs |
| --- | --- |
| `CLAUDE.md` | Project overview, build and lint commands, architecture, conventions (Room migrations, EventBus, background work, caching, settings, backup models), changelog and translation rules |
| `.editorconfig` | Formatting: 4 spaces, max line length 160, LF |
| `detekt.yml`, `app/detekt-baseline.xml` | Static analysis. New code must not add findings beyond the baseline |
| `lint.xml`, `app/lint-baseline.xml` | Android lint rules and baseline |
| `gradle/libs.versions.toml`, `gradle.properties` | Dependency versions and app version/ID |
| `CHANGELOG.md` | Keep a Changelog format. Add user-facing changes under `## [Unreleased]` |

Rules to remember:

- Match the surrounding code's style, naming, idiom and comment density.
- Edit only the default `values/strings.xml`. The translation service updates `values-*/strings.xml`.
- Do not edit `.tidings/release-marker.txt`. It is auto-generated.
- Changing a Room entity requires a `version` bump and a `MIGRATION_x_y`.

---

# Part B: General guidelines

Tidings is a client-only Android app. If sync via WebDAV, CalDAV or CardDAV is added, the server handles the server side. The app only speaks the client side of the protocol. Do not write guidance or code for server concerns.

## 2. Role and conduct

- You are a top-class software developer.
- You always follow best practices and guidelines for writing software.
- You always write test-driven code, where the project's setup allows it (see Part A).
- You give utmost priority to software security.
- You never commit directly to protected branches: `develop`, `main`, `master`.
- Readable code is your utmost priority.
- You understand topics well before acting on them.

## 3. Documentation

### 3.1 What to document

- For any change, document the design along with the **what**, **why** and **how**.

### 3.2 How to write it

- Use plain, simple language.
- Use as many diagrams as possible. `draw.io` is the first choice.
- Explain designs with easy-to-understand examples, ideally relatable to day-to-day life.
- Code should be self-explanatory. Never use documentation or comments to compensate for unclear code.

### 3.3 How to organize it

- Organize documents into folders and sub-folders.
- Reference other documents inside the repository and on the public internet where useful.
- Put a **summary** at the top of every document describing what it is about. Update it whenever the document changes enough to warrant it.
- Put **keywords** in every document. Update them the same way as the summary.
- Maintain the project docs: README and ADRs (architecture decision records).

## 4. Design principles

- **SOLID**: single responsibility, open/closed, Liskov substitution, interface segregation, dependency inversion
- **DRY**: don't duplicate knowledge, but don't force abstractions too early
- **KISS**: prefer the simplest solution that works
- **YAGNI**: don't build for needs you don't have yet
- **Separation of concerns**: UI, business logic and data access stay apart
- **Composition over inheritance**
- **Law of Demeter**: talk to your direct collaborators, not their internals
- **High cohesion, low coupling**
- **Principle of least surprise**: code should behave as readers expect

### Other principles worth knowing

- **Fail-safe defaults** and **defense in depth** (security)
- **Tell, don't ask**
- **Command-query separation**
- **Design by contract**: preconditions, postconditions, invariants
- **Clean Architecture / Hexagonal**: business logic independent of frameworks

## 5. Code quality

- Use meaningful, simple names. Keep functions small and single-purpose.
- Comments explain *why*, not *what*.
- Keep style consistent, enforced by linter and formatter.
- Avoid magic numbers and deep nesting (use early returns).
- Prefer immutability and pure functions where practical.
- Refactor continuously (Boy Scout rule: leave code cleaner than you found it).

## 6. Configurability

- Never hardcode URLs, credentials or limits. Put user-facing options in `Config.kt` and shared constants in `Constants.kt`.
- Give settings sane defaults.

## 7. Security and privacy

- Validate and sanitize all input, including incoming messages, vCards and imported backups. Use parameterized queries and Room DAOs.
- Never commit secrets (keystore files, passwords, tokens).
- Use HTTPS for any network access.
- Request only the permissions the app needs.
- Keep dependencies updated and scanned.

## 8. Logging

- Use correct log levels.
- Never log message content, phone numbers, contact details or credentials.

## 9. Error handling and resilience

- Fail fast. Never swallow exceptions silently.
- Make operations safe to retry (for example sending, import and sync).
- Degrade gracefully, for example when permissions are denied or the network is unavailable.

## 10. Testing

- Follow the test pyramid: many unit tests, fewer integration tests, few end-to-end.
- Write testable code (dependency injection).
- Cover edge cases and failure paths, not just the happy path.
- CI runs tests on every commit to the remote repository.

## 11. Performance

- Measure before optimizing (avoid premature optimization).
- Keep the main thread free: do database, provider and network work in the background.
- Watch for N+1 queries. Add proper indexes. Use caching and pagination for large conversation lists.
- Mind battery and memory use.

## 12. Data

- Room migrations are versioned (see Part A).
- Keep the JSON backup format backward compatible, and test import of older backups.

## 13. Process and team

- Make small commits with clear messages. Get code reviews.
- Use CI/CD.
- Keep technical debt visible and manage it deliberately.

## 14. Memory and the wiki

- Document everything in the wiki.
- Structure wiki documentation in folders and cross-reference related documents to reduce duplication and ease navigation.
- Search the wiki before starting any new architecture.
- Search the wiki only for repo-specific context you don't already have: business context, how our software works, past decisions. Do not search for general knowledge, small talk, or anything already answered in the conversation.
- Search once per question. Search again only if the results clearly missed. Never repeat a query.
