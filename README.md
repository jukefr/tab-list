# Tab List

**Customize your Minecraft server's tab list header, footer, and welcome message — on Fabric and NeoForge.**

[![Modrinth](https://img.shields.io/modrinth/dt/tab?style=flat-square&logo=modrinth&label=Modrinth)](https://modrinth.com/mod/tab)
[![GitHub release](https://img.shields.io/github/v/release/jukefr/tab-list?style=flat-square&logo=github)](https://github.com/jukefr/tab-list/releases)
[![License](https://img.shields.io/badge/license-GPL--3.0-blue?style=flat-square)](https://github.com/jukefr/tab-list/blob/main/LICENSE)

![Tab List showing a custom server header, TPS, MSPT, player count, uptime, and a message of the day](https://raw.githubusercontent.com/jukefr/tab-list/main/resources/screenshot.png)

Tab List is a lightweight, server-side Minecraft mod for **Fabric** and **NeoForge**. It lets you
replace the player list header and footer with your own text, colors, and live server stats, and
send a message of the day when players join. No client mod is required.

## Features

- Custom tab list **header** and **footer**
- **MOTD** sent on player login
- Live placeholders for **TPS**, **MSPT**, **player count**, and **uptime**
- Full Minecraft formatting code support (`§` colors and styles, plus `#N` for new lines)
- Adjustable refresh interval
- Works on **Fabric** and **NeoForge**
- Same simple JSON config on both loaders

## Installation

1. Install **Fabric Loader** or **NeoForge** on your Minecraft server.
2. Download the jar for your Minecraft version from [Modrinth](https://modrinth.com/mod/tab) or
   [GitHub Releases](https://github.com/jukefr/tab-list/releases).
3. Put the jar in your server's `mods/` folder.
4. On Fabric, also install [Fabric API](https://modrinth.com/mod/fabric-api).
5. Start the server. The config file is created at `config/fabric-tab.json`.
6. Edit the config and restart the server.

> This is a **server-side** mod. Players do not need to install anything.

## Supported versions

| Loader | Minecraft versions |
|---|---|
| Fabric | 1.16.5, 1.18–1.18.2, 1.19–1.19.4, 1.20–1.20.1, 1.21–1.21.11, 26.1–26.3 |
| NeoForge | 1.21.1, 1.21.4, 1.21.8, 1.21.11, 26.1.2, 26.2, 26.3 |

Use the jar that matches your Minecraft version. NeoForge 26.1 support is for **26.1.2**;
NeoForge does not publish builds for 26.1 or 26.1.1.

## Configuration

The config file is `config/fabric-tab.json`:

```json
{
  "configVersion": 2,
  "header": "§7§l§oCOOL#N      SERVER       #N#N§6very cool§f#N",
  "footer": "#N§b#TPS tps - #MSPT mspt - #PLAYERCOUNT players - #UPTIME uptime#N",
  "motd": "§7§oWelcome to the server.§f",
  "refreshTickInterval": 20,
  "enabled": true
}
```

| Key | Type | Description |
|---|---|---|
| `configVersion` | number | Internal version. Do not change. |
| `header` | string | Text shown above the player list. |
| `footer` | string | Text shown below the player list. |
| `motd` | string | Message sent to players when they join. |
| `refreshTickInterval` | number | Ticks between header/footer updates. `20` = about 1 second. Values of `0` or less fall back to `20`. |
| `enabled` | boolean | Enables or disables the mod. |

The config is loaded at server startup, so restart the server after editing it.

On Minecraft 1.16.5–1.19.4 the config uses `configVersion` 1 and only contains `header`,
`footer`, and `motd`; the tab list is refreshed every tick on those versions.

The MOTD is sent as-is: placeholders are not processed inside `motd`.

## Placeholders

Use these inside `header` and `footer`:

| Placeholder | Replaced with |
|---|---|
| `#TPS` | Current server TPS (capped at 20) |
| `#MSPT` | Average milliseconds per tick |
| `#PLAYERCOUNT` | Number of online players |
| `#UPTIME` | Server process uptime |
| `#N` | A new line |

Placeholders are case-sensitive.

Example footer:

```text
#N§b#TPS tps §7| §b#MSPT mspt §7| §b#PLAYERCOUNT players §7| §b#UPTIME uptime#N
```

## Formatting

Minecraft legacy formatting codes work normally:

```text
§0 black       §1 dark blue    §2 dark green   §3 dark aqua
§4 dark red    §5 dark purple  §6 gold         §7 gray
§8 dark gray   §9 blue         §a green        §b aqua
§c red         §d light purple §e yellow       §f white

§k obfuscated  §l bold         §m strikethrough
§n underline   §o italic       §r reset
```

## FAQ

**Does this work on a client?**
No client mod is needed. Install it on the server only.

**Does Fabric require Fabric API?**
Yes. NeoForge does not need an extra API mod.

**Do changes apply without a restart?**
No. The config is read at startup, so restart the server after editing it.

**Can I use the MOTD placeholders?**
No. Placeholders apply to the header and footer only. The MOTD is sent exactly as written.

**Where do I report bugs or request features?**
Use the [issue tracker](https://github.com/jukefr/tab-list/issues).

## Links

- [Modrinth](https://modrinth.com/mod/tab)
- [GitHub Releases](https://github.com/jukefr/tab-list/releases)
- [Source code](https://github.com/jukefr/tab-list)
- [Issues](https://github.com/jukefr/tab-list/issues)

## License

GPL-3.0. See [LICENSE](https://github.com/jukefr/tab-list/blob/main/LICENSE).
