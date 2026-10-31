# Sarcio

Sarcio is a mod for Minecraft 1.8.9 with [Ornithe](https://ornithemc.net/) that includes a plethora of memory leak fixes, 
memory usage optimizations, bugfixes, and general tweaks, driven by real profiling data and user need.

## Features

- **Memory leak fixes**: memory gets released properly instead of piling up over a long session
- **Lower memory usage**: less RAM used by worlds, chunks, textures, and networking
- **Fewer allocations**: less garbage created each frame, so fewer lag spikes from garbage collection
- **Bugfixes**: fixes for long-standing vanilla bugs in rendering, entities, sounds, GUIs, and more
- **Tweaks**: faster server list pinging, and the option to hide the Realms button
- **Full list**: every change is described in [FEATURES.md](FEATURES.md)
- **Config**: Options are under `config/sarcio.json`, and with [Argentum](https://github.com/QuicksilverMC/Argentum), they're also in the video settings menu.

## Disabling features from another mod

A mod that replaces something Sarcio patches can ask Sarcio to leave specific patches out. Every patch is an
`@Feature` in the source, named `<category>.<patch>`, and may span several mixins or just part of one. In that mod's
`fabric.mod.json`, list them under `custom`:

```json
"custom": {
  "sarcio:disable": ["bugfix.render.fluid_gaps", "bugfix.render.deleted_vertex_buffer"]
}
```
