# AGENTS.md

Instructions for future agents working on this repository.

## Project overview

Tab List (`tab-list`) is a small **server-side only** mod for **Fabric and NeoForge**
that customizes the player list (tab list) header/footer and sends a MOTD on login.

- Display/project name: Tab List
- Internal Fabric mod id: `fabric-tab`; internal NeoForge mod id: `fabric_tab`
  (NeoForge ids cannot contain `-`)
- Fabric main class: `fr.juke.FabricTab` (on old Fabric branches it is `fr.juke.Main`)
- NeoForge main class: `fr.juke.FabricTab`
- Config file: `config/fabric-tab.json` (same file on both loaders)
- GitHub: https://github.com/jukefr/tab-list
- Modrinth: https://modrinth.com/mod/tab (project id `9J38edBm`, slug stays `tab`)

The display name changed from "Fabric Tab List" to "Tab List". Do **not** rename the internal
mod ids, config file, or jar prefix (`fabric-tab`) without a compatibility/migration plan.

## Writing the README and project pages

`README.md` is the public landing page for GitHub and is mirrored to the Modrinth project body.
Write for someone who just wants to install the mod, then for someone searching for a specific
feature. Optimize for useful search terms, not keyword density.

### Required structure

1. `# Tab List` as the H1.
2. A one-line tagline containing the important terms:
   Minecraft, tab list, header, footer, MOTD, Fabric, NeoForge.
3. The screenshot near the top, with descriptive alt text. Keep the screenshot.
4. Optional badges: Modrinth downloads, latest GitHub release, license. Do not add badge walls.
5. A short paragraph explaining what the mod does and that it is server-side only.
6. Features.
7. Installation / quick start.
8. Supported versions.
9. Configuration.
10. Placeholders.
11. Formatting codes.
12. A short FAQ for common confusion.
13. Links.
14. License.

### Content rules

- Keep the README accurate. Do not advertise Minecraft versions that have no release.
- Use natural phrases such as "Minecraft tab list", "player list header and footer",
  "TPS and MSPT placeholders", "Fabric tab list", and "NeoForge tab list". Do not stuff them.
- Put the quick start near the top. Users should not have to scroll past marketing copy.
- Show the real config format, matching the code. Explain that 1.16.5–1.19.4 use config v1 and
  1.20+ use config v2.
- Include a copy-paste footer example using `#TPS`, `#MSPT`, `#PLAYERCOUNT`, `#UPTIME`, and `#N`.
- State clearly that the mod is server-side only, that Fabric needs Fabric API, and that the
  server must be restarted after config changes.
- State that MOTD is sent as-is and does not process placeholders.
- Use absolute links (`https://github.com/jukefr/tab-list/...` and
  `https://modrinth.com/mod/tab`). Relative links work on GitHub but break on Modrinth.
- Do not add fake benchmarks, roadmap promises, giant changelogs, or generic filler.
- Do not remove the screenshot or replace it with a text-only description.
- Keep the FAQ short and solve real problems: client-side confusion, Fabric API, reloads,
  MOTD placeholders, bug reports.

### Syncing Modrinth

The Modrinth page keeps the slug `tab` and must stay in sync with the README:

- Project title: `Tab List`
- Body: contents of `README.md`
- Source URL: `https://github.com/jukefr/tab-list`
- Issues URL: `https://github.com/jukefr/tab-list/issues`

After changing the README, update the Modrinth project body with
`PATCH /v2/project/9J38edBm`. Keep the slug and don't publish a new version just for docs unless
the release notes also changed.

Also update the GitHub repository description and homepage when the positioning changes.

## The most important rule

**Do not change the config file format unless explicitly asked to.**

That means:

- Keep the same JSON keys.
- Keep the same defaults.
- Keep `configVersion` semantics.
- Keep the `#TPS`, `#MSPT`, `#UPTIME`, `#PLAYERCOUNT`, `#N` placeholders working the same way.
- Keep MOTD unformatted (it does not go through `TextFormatter`).

Current config versions:

- `configVersion` 1: Minecraft 1.16.5–1.19.4. Fields:
  `configVersion`, `header`, `footer`, `motd`.
- `configVersion` 2: Minecraft 1.20+. Adds:
  `refreshTickInterval`, `enabled`.

If a schema change is ever required, bump `configVersion` and add a migration path.

## Branch layout

`main` always tracks the **latest supported Minecraft version** (currently 26.3).

There is one branch per Minecraft version, with `main` also pointing at the latest version:

| Branch range | Notes |
|---|---|
| `1.16.5` | Oldest supported version, special code |
| `1.18` – `1.18.2` | Config v1 |
| `1.19` – `1.19.4` | Config v1 |
| `1.20` – `1.20.1` | Config v2 |
| `1.21` | Old Loom/Gradle toolchain |
| `1.21.1` – `1.21.11` | Modern Loom, Yarn mappings |
| `26.1` – `26.1.2` | Mojang mappings/unobfuscated |
| `26.2` – `26.3` | Mojang mappings/unobfuscated |

NeoForge support lives on separate branches, one per supported NeoForge line:

| NeoForge branch | Minecraft version | NeoForge version |
|---|---|---|
| `neoforge-1.21.1` | 1.21.1 | `21.1.268` |
| `neoforge-1.21.4` | 1.21.4 | `21.4.161` |
| `neoforge-1.21.8` | 1.21.8 | `21.8.57` |
| `neoforge-1.21.11` | 1.21.11 | `21.11.48` |
| `neoforge-26.1.2` | 26.1.2 | `26.1.2.130` |
| `neoforge-26.2` | 26.2 | `26.2.0.95` |
| `neoforge-26.3` | 26.3 | `26.3.0.118` |

NeoForge only publishes for 26.1.2, not 26.1 or 26.1.1, so the NeoForge 26.1 support is
exactly 26.1.2.

Branches older than 26.x use Yarn mappings. Minecraft 26.x has no Yarn mappings and ships
unobfuscated, so those branches use the official Mojang names and the non-remap Loom plugin.
NeoForge branches use Mojang names for every version.

### Branch/tag name ambiguity

Every branch also has a tag with the same name (for example branch `1.18.2` and tag `1.18.2`).
Git will warn that a ref is ambiguous and commands like `git show 1.18.2:file` resolve to the
**tag**, which is the old historical commit.

Use explicit refs when inspecting branches:

```bash
git show refs/heads/1.18.2:src/main/resources/fabric.mod.json
git log --oneline refs/heads/1.18.2
```

When switching, verify where you ended up:

```bash
git switch 1.18.2
git symbolic-ref --short HEAD   # should print 1.18.2
```

If a branch is missing locally but exists on the remote:

```bash
git branch 1.18.2 origin/1.18.2
```

## Toolchains by era

Use the JDK that matches the Gradle version. Getting this wrong causes confusing
"JVM runtime version" errors.

| Version range | Gradle | Build JDK | Loom / mappings | Java target |
|---|---|---|---|---|
| 1.16.5 | 7.4.2 | 17 | Loom 1.0-SNAPSHOT, Yarn | 17 |
| 1.18 – 1.19.4 | 7.4.2 | 17 | Loom 1.0-SNAPSHOT, Yarn | 17 |
| 1.20 – 1.20.1 | 7.4.2 | 17 | Loom 1.0-SNAPSHOT, Yarn | 17 |
| 1.21 | 8.8 | 21 | Loom 1.7-SNAPSHOT, Yarn | 21 |
| 1.21.1 – 1.21.11 | 9.8.2 | **25** | Loom 1.19.4, Yarn | 21 |
| 26.1 – 26.3 | 9.8.2 | **25** | Loom 1.19.4, Mojang names | 25 |

NeoForge branches have a different toolchain:

| NeoForge range | Gradle | Build JDK | Plugin | Java target |
|---|---|---|---|---|
| `neoforge-1.21.1` – `neoforge-1.21.11` | 9.2.1 | 21 | ModDevGradle 2.0.148 | 21 |
| `neoforge-26.1.2` – `neoforge-26.3` | 9.2.1 | 25 | ModDevGradle 2.0.148 | 25 |

NeoForge builds take longer on the first run because ModDevGradle decompiles and patches
Minecraft for each NeoForge version.

Important: Loom 1.19.4 requires **Java 25 to run Gradle**, even on branches whose compiled
release target is Java 21.

Build examples:

```bash
# Old branches
JAVA_HOME=/usr/lib/jvm/java-17-openjdk \
PATH=/usr/lib/jvm/java-17-openjdk/bin:$PATH \
./gradlew build --no-daemon

# 1.21 only
JAVA_HOME=/usr/lib/jvm/java-21-openjdk \
PATH=/usr/lib/jvm/java-21-openjdk/bin:$PATH \
./gradlew build --no-daemon

# 1.21.1+ and 26.x
JAVA_HOME=/usr/lib/jvm/java-25-openjdk \
PATH=/usr/lib/jvm/java-25-openjdk/bin:$PATH \
./gradlew build --no-daemon

# NeoForge 1.21.x
JAVA_HOME=/usr/lib/jvm/java-21-openjdk \
PATH=/usr/lib/jvm/java-21-openjdk/bin:$PATH \
./gradlew build --no-daemon

# NeoForge 26.x
JAVA_HOME=/usr/lib/jvm/java-25-openjdk \
PATH=/usr/lib/jvm/java-25-openjdk/bin:$PATH \
./gradlew build --no-daemon
```

NeoForge jars are `build/libs/fabric-tab-<version>.jar` as well.

## Code layout differences

### 1.16.5

- Entrypoint: `fr.juke.Main`
- Utility package: `fr.juke.commands.util`
- Text API uses `LiteralText`
- The tab packet has no public constructor, so `PlayerListMixin` is an accessor-only mixin
  that sets `header`/`footer`
- Config v1

### 1.18 – 1.19.4

- Entrypoint: `fr.juke.Main`
- Utility package: `fr.juke.commands.util`
- 1.18 uses `Text.of(...)`; 1.19+ uses `Text.literal(...)`
- Config v1
- No mixins; tab list is sent from `ServerTickEvents.END_SERVER_TICK`

### 1.20 – 1.21.11

- Entrypoint: `fr.juke.FabricTab`
- Utility package: `fr.juke.util`
- Config v2
- No mixins; uses `ServerTickEvents` and `ServerPlayConnectionEvents`
- 1.20–1.20.1 use `MinecraftServer.getTickTime()`
- 1.21+ use `MinecraftServer.getAverageTickTime()`

### 26.x

- Official Mojang names, no Yarn mappings
- `net.fabricmc.fabric-loom` plugin, no `mappings` line
- `implementation` instead of `modImplementation` in `build.gradle`
- Examples:
  `PlayerList`, `ServerPlayer`, `Component`, `ClientboundTabListPacket`,
  `MinecraftServer.getPlayerList()`, `broadcastAll(...)`, `sendSystemMessage(...)`,
  `getAverageTickTimeNanos()`
- Config v2

### NeoForge

- Uses Mojang names on every version (NeoForge ships Mojang mappings)
- Build system: `net.neoforged.moddev` 2.0.148, Gradle 9.2.1
- No mixins and no `fabric.mod.json`
- Metadata template: `src/main/templates/META-INF/neoforge.mods.toml`
- Main class `fr.juke.FabricTab` is annotated with `@Mod(FabricTab.MOD_ID)`
- Event hooks:
  - `NeoForge.EVENT_BUS.addListener(this::onServerTick)` with `ServerTickEvent.Post`
  - `NeoForge.EVENT_BUS.addListener(this::onPlayerLogin)` with
    `PlayerEvent.PlayerLoggedInEvent`
- Config path: `FMLPaths.CONFIGDIR.get().resolve("fabric-tab.json")`
- Tab list packet: `ClientboundTabListPacket`, sent with
  `server.getPlayerList().broadcastAll(...)`
- MOTD: `player.sendSystemMessage(Component.literal(motd))`
- Config v2
- NeoForge 1.21.x TOML includes `modLoader` and `loaderVersion`; the 26.x TOML does not.
- The NeoForge mod id is `fabric_tab`; the config file name stays `fabric-tab.json`.

## Git workflow

1. Make the change on the latest applicable branch first (usually `main` / `26.3`).
2. Backport the equivalent change to every affected branch. Keep each era's API naming,
   package layout and config version intact.
3. Build each distinct era variant and each release endpoint.
4. Commit per branch.
5. Bump `mod_version` in `gradle.properties` on all affected branches.
6. Update the `minecraft` dependency range in each `fabric.mod.json`.
7. Tag the release endpoints.
8. Create GitHub and Modrinth releases.

For isolated builds, use temporary worktrees, then remove them:

```bash
git worktree add -f /tmp/opencode/ft-1.18.2 1.18.2
# work there
rm -rf /tmp/opencode/ft-1.18.2/build /tmp/opencode/ft-1.18.2/.gradle
git worktree remove --force /tmp/opencode/ft-1.18.2
```

Because branches share tags, `git worktree add ... 1.18.2` may check out the tag in detached
HEAD. Commit only after confirming `git symbolic-ref --short HEAD` is the branch name, or
create the branch explicitly with `git worktree add -b <branch> <dir> origin/<branch>`.

## Supported release ranges

Releases are grouped by compatibility line, not one release per patch version:

| Release key | Supported versions | Built from branch |
|---|---|---|
| `1.16.5` | 1.16.5 | `1.16.5` |
| `1.18` | 1.18, 1.18.1, 1.18.2 | `1.18.2` |
| `1.19` | 1.19 – 1.19.4 | `1.19.4` |
| `1.20` | 1.20, 1.20.1 | `1.20.1` |
| `1.21` | 1.21 – 1.21.11 | `1.21.11` |
| `26.1` | 26.1, 26.1.1, 26.1.2 | `26.1.2` |
| `26.2` | 26.2, 26.3 | `26.3` |

Current version: `2.2.0`.

`fabric.mod.json` ranges must match the group:

- 1.16.5: `"1.16.5"`
- 1.18: `">=1.18 <1.19"`
- 1.19: `">=1.19 <1.20"`
- 1.20: `">=1.20 <1.21"`
- 1.21: `">=1.21 <1.22"`
- 26.1: `">=26.1 <26.2"`
- 26.2: `">=26.2 <26.4"`

The jar for a range is built from the **latest version in that range**.

### NeoForge release mapping

NeoForge builds are attached to the existing GitHub release for the matching Fabric range.
They do **not** get separate GitHub tags or releases.

| GitHub release | NeoForge assets |
|---|---|
| `2.2.0+1.21` | 1.21.1, 1.21.4, 1.21.8, 1.21.11 |
| `2.2.0+26.1` | 26.1.2 |
| `2.2.0+26.2` | 26.2, 26.3 |

On Modrinth, NeoForge gets separate versions because the loader differs:

- `2.2.0+1.21.1-neoforge`
- `2.2.0+1.21.4-neoforge`
- `2.2.0+1.21.8-neoforge`
- `2.2.0+1.21.11-neoforge`
- `2.2.0+26.1.2-neoforge`
- `2.2.0+26.2-neoforge`
- `2.2.0+26.3-neoforge`

Use exact game versions for NeoForge (`["1.21.1"]`, not ranges). NeoForge does not support
1.16.5–1.20.1, so there are no NeoForge builds for the older Fabric lines.

## GitHub releases

Tags for grouped releases use this format:

```
2.2.0+1.16.5
2.2.0+1.18
2.2.0+1.19
2.2.0+1.20
2.2.0+1.21
2.2.0+26.1
2.2.0+26.2
```

Create a tag on the endpoint branch:

```bash
git tag "2.2.0+1.18" refs/heads/1.18.2
git push origin "2.2.0+1.18"
```

Create the release:

```bash
gh release create "2.2.0+1.18" \
  "fabric-tab-2.2.0+mc1.18.x.jar" \
  -R jukefr/tab-list \
  --title "2.2.0 - 1.18 - 1.18.2" \
  --notes "Tab List 2.2.0 for Minecraft 1.18 through 1.18.2." \
  --latest=false
```

Mark only the newest release with `--latest` (currently `2.2.0+26.2`).

Asset naming convention:

```
fabric-tab-<modversion>+mc<range>.jar
```

Examples: `fabric-tab-2.2.0+mc1.18.x.jar`, `fabric-tab-2.2.0+mc26.2-26.3.jar`.

NeoForge assets use the same release tag as the matching Fabric range:

```bash
gh release upload "2.2.0+1.21" \
  fabric-tab-2.2.0+mc1.21.1-neoforge.jar \
  fabric-tab-2.2.0+mc1.21.4-neoforge.jar \
  fabric-tab-2.2.0+mc1.21.8-neoforge.jar \
  fabric-tab-2.2.0+mc1.21.11-neoforge.jar \
  -R jukefr/tab-list
```

The release notes should list which NeoForge versions are in the release.

When replacing an older release series, delete the superseded releases:

```bash
gh release delete "<old-tag>" -R jukefr/tab-list --yes
```

Old tags can stay for history.

## Modrinth releases

Modrinth has an API for this. The project id is `9J38edBm` (slug `tab`).

The Modrinth project page body should mirror `README.md`. When the README changes, update the
project body with `PATCH /v2/project/9J38edBm` and a JSON field named `body`. Do not change the
slug; it must stay `tab`.

### Token

The token lives in `.env` at the repository root:

```
MODRINTH_TOKEN=mrp_...
```

- `.env` must stay `chmod 600`.
- `.env` is gitignored. Never print, commit, or paste the token.
- The PAT needs the **`VERSION_CREATE`** scope.

Do **not** test the token with `GET /v2/user`. That endpoint needs `USER_READ` and will return
`401` even when the token is valid for publishing. Test by creating a version (or by checking
for a validation error rather than `401`).

Load the token without echoing it:

```bash
set -a
. ./.env
set +a
```

### Create a version

`POST https://api.modrinth.com/v2/version` is a multipart request:

- `data`: JSON metadata
- one or more file fields listed in `file_parts`
- `Authorization: <token>` header (Bearer also works)
- a descriptive `User-Agent` is required by Modrinth

Example:

```bash
UA="jukefr/tab-list-publisher/1.0 (code@juke.fr)"
DATA='{
  "project_id": "9J38edBm",
  "name": "2.2.0 for 1.18 - 1.18.2",
  "version_number": "2.2.0+1.18",
  "changelog": "Robust config handling, fixed TPS/MSPT calculation, and Fabric API event-based tab list updates.",
  "game_versions": ["1.18", "1.18.1", "1.18.2"],
  "version_type": "release",
  "loaders": ["fabric"],
  "environment": "server_only",
  "featured": false,
  "status": "listed",
  "file_parts": ["file"],
  "primary_file": "file",
  "dependencies": []
}'

curl -X POST "https://api.modrinth.com/v2/version" \
  -A "$UA" \
  -H "Authorization: $MODRINTH_TOKEN" \
  -F "data=$DATA;type=application/json" \
  -F "file=@fabric-tab-2.2.0+mc1.18.x.jar;type=application/java-archive"
```

Rules:

- `version_number` must be unique per project. Use `<modversion>+<range>` for Fabric and
  `<modversion>+<mcversion>-neoforge` for NeoForge.
- Each version lists every supported game version in `game_versions`. NeoForge versions use
  exact game versions, not ranges.
- `loaders` is `["fabric"]` for Fabric and `["neoforge"]` for NeoForge.
- `environment` is `server_only`.
- `version_type` is `release`.
- Do not publish alpha/beta/snapshot Minecraft versions.

Verify:

```bash
curl -s https://api.modrinth.com/v2/project/tab/version | \
  python3 -m json.tool | head -40
```

### Archive superseded versions

After publishing a new release series, archive every Modrinth version that is older than the
new series. Do **not** delete them: archived versions remain downloadable by direct link, so
users on old setups keep access, but they are no longer presented as current.

The 2.2.0 release archived all 2.1.0, 2.0.x and 1.4.0 versions. Follow the same rule for future
releases: keep only the versions belonging to the newest release series listed.

Archiving needs the **`VERSION_WRITE`** scope (separate from `VERSION_CREATE`). The current PAT
has both.

Archive one version:

```bash
curl -X PATCH "https://api.modrinth.com/v2/version/<version-id>" \
  -A "jukefr/tab-list-publisher/1.0 (code@juke.fr)" \
  -H "Authorization: $MODRINTH_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"archived"}'
```

A successful request returns `204 No Content`.

Archive all old versions in one pass:

```bash
curl -s https://api.modrinth.com/v2/project/tab/version > versions.json

python3 - <<'EOF' > old_versions.txt
import json
for v in json.load(open("versions.json")):
    if not v["version_number"].startswith("<new-version>+"):
        print(v["id"], v["version_number"])
EOF

while read -r id version; do
  code=$(curl -s -o /dev/null -w '%{http_code}' -X PATCH \
    "https://api.modrinth.com/v2/version/$id" \
    -A "jukefr/tab-list-publisher/1.0 (code@juke.fr)" \
    -H "Authorization: $MODRINTH_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"status":"archived"}')
  echo "$version -> $code"
done < old_versions.txt
```

Note: the project version-list endpoint can return stale cached statuses for a short while. Check
individual versions to confirm:

```bash
curl -s "https://api.modrinth.com/v2/version/<version-id>" | \
  python3 -c "import json,sys; d=json.load(sys.stdin); print(d['version_number'], d['status'])"
```

## Release checklist

1. Confirm `main` is at the latest supported version (`26.3`).
2. Confirm every branch builds with its era's JDK.
3. Bump `mod_version` in `gradle.properties` on every branch being released.
4. Make sure each loader metadata file has the correct Minecraft version/range and Java
   requirement (`fabric.mod.json` for Fabric, `neoforge.mods.toml` for NeoForge).
5. Build each Fabric range endpoint and each NeoForge version, and verify the jar metadata:
   ```bash
   unzip -p build/libs/fabric-tab-<ver>.jar fabric.mod.json       # Fabric
   unzip -p build/libs/fabric-tab-<ver>.jar META-INF/neoforge.mods.toml  # NeoForge
   ```
6. Commit and push all branches:
   ```bash
   git push origin --all
   ```
7. Create and push grouped tags for Fabric ranges.
8. Create GitHub releases for every Fabric range, and upload the matching NeoForge jars to those
   releases.
9. Create the equivalent Modrinth versions for both loaders.
10. Archive every Modrinth version older than the new release series (see above).
11. Verify both release pages:
    - only the newest GitHub release is marked latest;
    - only versions from the newest release series are `listed` on Modrinth.

## Common pitfalls

- **Ambiguous branch/tag refs**: `git show <name>` uses the tag. Use `refs/heads/<name>`.
- **Wrong JDK**: Loom 1.19.4 needs JDK 25 to run Gradle, even for Java 21 targets.
- **26.x mappings**: no Yarn, no `mappings` line, use `net.fabricmc.fabric-loom` and
  `implementation`, not `modImplementation`.
- **Fabric API dependency id**: old branches use `"fabric": "*"`; 1.21+ use `"fabric-api": "*"`.
- **Modrinth 401 on `/v2/user`**: expected if the PAT lacks `USER_READ`; it does not mean the
  token is invalid.
- **Modrinth old versions**: archive them after a new release series ships, do not delete them.
  Keep only the newest series `listed`.
- **NeoForge mod id**: NeoForge ids cannot contain `-`. It is `fabric_tab`, not `fabric-tab`.
  The config file is still `fabric-tab.json`.
- **NeoForge 26.x metadata**: the 26.x `neoforge.mods.toml` omits `modLoader` and
  `loaderVersion`; 1.21.x includes them.
- **NeoForge version coverage**: there are no NeoForge builds for 1.16.5–1.20.1, and NeoForge
  only publishes for 26.1.2 (not 26.1 or 26.1.1).
- **NeoForge first build**: ModDevGradle decompiles/patches Minecraft and can take several
  minutes. Later builds are fast.
- **Config compatibility**: never rename or remove config fields without a version bump and
  migration.
- **MOTD formatting**: MOTD intentionally does not run through `TextFormatter`.
- **Release scope**: build the range jar from the latest version in the range, not from the
  oldest.
- **`.env`**: keep it out of git and out of logs.
