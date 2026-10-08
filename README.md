# Sarcio

Sarcio is a mod for Minecraft 1.8.9 with [Ornithe](https://ornithemc.net/) that includes a plethora of memory leak fixes, 
memory usage optimizations, bugfixes, and general tweaks, driven by real profiling data and user need.

## Features

- **Memory leak fixes**: memory gets released properly instead of piling up over a long session
- **Lower memory usage**: less RAM used by worlds, chunks, textures, and networking
- **Fewer allocations**: less garbage created each frame, so fewer lag spikes from garbage collection
- **Bugfixes**: fixes for long-standing vanilla bugs in rendering, entities, sounds, GUIs, and more
- **Tweaks**: faster server list pinging, and the option to hide the Realms button
- **Config**: With [Argentum](https://github.com/QuicksilverMC/Argentum), options are in the video settings menu; they're also under `config/sarcio.json`.

## Disabling mixins from another mod

A mod that replaces something Sarcio patches can ask Sarcio to leave specific mixins out. In that mod's
`fabric.mod.json`, list them under `custom`, by name relative to `dev.rdh.sarcio.mixin`:

```json
"custom": {
  "sarcio:disable": ["bugfix.render.WorldRendererMixin", "bugfix.render.VertexBufferMixin"]
}
```
