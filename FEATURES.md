# Features

Everything Sarcio changes, grouped by category. The names in `code` are what other mods use to switch a change off.

Sarcio also replaces Minecraft's outdated Netty with a current version, which fixes networking issues.
This is always on, and can't be switched off.

<details>
<summary><b>Bugfixes</b></summary>

### General

- `bugfix.ascii_digits`: stops the game breaking in languages that don't write numbers with 0-9.
- `bugfix.create_server_pack_dir`: creates the server resource pack folder if it's missing.
- `bugfix.mouse_bound_keys`: perspective, fullscreen, screenshot, inventory, drop and hotbar keys work on mouse buttons.
- `bugfix.nanosecond_tick_timer`: the game keeps time more accurately.
- `bugfix.nbt_null_fail_fast`: bad NBT data fails right away, with a useful error.
- `bugfix.persistent_keybinds`: keys you hold through a menu stay held when it closes. Has a setting.
- `bugfix.save_fullscreen`: remembers fullscreen after toggling it.
- `bugfix.silence_task_errors`: less log spam from failed background tasks.
- `bugfix.startup_progress_screen`: shows a progress screen while a singleplayer world starts.
- `bugfix.thread_safe_lazy_supplier`: fixes a rare crash from values being loaded by two threads at once.

### Entities

- `bugfix.entity.ground_sprint_particles`: sprinting only kicks up particles on the ground.
- `bugfix.entity.missing_status_effect`: fixes a crash from missing status effects.
- `bugfix.entity.mouse_delay_fix`: removes the delay between where you look and where you hit.
- `bugfix.entity.oob_brightness`: entities outside loaded chunks aren't drawn black.
- `bugfix.entity.orb_target_height`: experience orbs fly to your body instead of your eyes.

### Menus and text

- `bugfix.gui.armor_break_sound`: plays a sound when your armor breaks.
- `bugfix.gui.book_background`: books get the usual dark background.
- `bugfix.gui.book_blending`: fixes transparency in books.
- `bugfix.gui.book_tooltip_order`: book tooltips are drawn on top.
- `bugfix.gui.chat_clicks_below`: clicking below the chat doesn't click hidden messages.
- `bugfix.gui.chat_null_line`: fixes a chat crash.
- `bugfix.gui.clone_drag_count`: shows the right count when drag-cloning items.
- `bugfix.gui.close_menu_on_click_outside`: fixes menus not closing properly with touchscreen mode.
- `bugfix.gui.close_unused_packs`: closes resource pack files when the pack screen is done with them.
- `bugfix.gui.connect_text_position`: connecting text stays above the Cancel button.
- `bugfix.gui.death_screen_clicks`: death screen buttons work right away.
- `bugfix.gui.deferred_mipmap_reload`: changing mipmaps only reloads once, when you leave video settings.
- `bugfix.gui.hex_tooltips`: colors in advanced tooltips show as proper hex codes.
- `bugfix.gui.high_potion_levels`: potion levels above IV show in the inventory.
- `bugfix.gui.ignore_unicode_keys`: fixes a crash from keybinds on unusual keys.
- `bugfix.gui.inventory_blending`: fixes transparency in inventories.
- `bugfix.gui.language_only_reload`: changing language no longer reloads every resource.
- `bugfix.gui.minute_stat_unit`: statistics say `min` for minutes instead of `m`.
- `bugfix.gui.opaque_scoreboard_text`: scoreboard text is solid instead of faded.
- `bugfix.gui.pack_folder_info_position`: moves the folder hint on the resource pack screen to where it fits.
- `bugfix.gui.rewrap_chat`: chat rewraps when the font changes.
- `bugfix.gui.save_options_on_close`: options save when you close them.
- `bugfix.gui.server_icon_crash`: broken server icons no longer crash the game.
- `bugfix.gui.server_list_entry_bounds`: fixes a server list crash.
- `bugfix.gui.sound_slider_save`: sound sliders save when released instead of while dragging, which caused lag.
- `bugfix.gui.spectator_pumpkin_overlay`: spectators don't see the pumpkin overlay.
- `bugfix.gui.stop_input_when_closed`: closed menus stop taking keyboard input.
- `bugfix.gui.tab_list_null_player`: fixes a player list crash.
- `bugfix.gui.text_style_reset`: text shadows no longer leak their style into the text.
- `bugfix.gui.trim_server_ip`: spaces around a typed server address are ignored.
- `bugfix.gui.unicode_glyph_offset`: fixes the spacing of characters missing from the default font.
- `bugfix.gui.unsigned_glyph_start`: fixes the spacing of some unicode characters.

### Models

- `bugfix.model.skeleton_arm_item`: items sit properly in skeletons' hands.
- `bugfix.model.slim_arm_item`: items sit properly in slim-armed players' hands.
- `bugfix.model.villager_robe_height`: fixes the length of villager robes.

### Multiplayer

- `bugfix.network.clear_titles_on_login`: titles from the last server don't carry over.
- `bugfix.network.drop_closed_connection_packets`: ignores packets from connections that have closed.
- `bugfix.network.drop_packets_without_world`: fixes crashes from packets arriving with no world loaded.
- `bugfix.network.entity_packets_without_world`: fixes crashes from entity packets arriving with no world loaded.
- `bugfix.network.hide_sign_debug`: hides the "Unable to locate sign" chat message.
- `bugfix.network.server_address_crash`: fixes a crash from invalid server addresses.
- `bugfix.network.server_list_bounds`: fixes server list crashes.

### Rendering

- `bugfix.render.banner_texture_lru`: banners keep working once too many designs have been seen.
- `bugfix.render.banner_time_precision`: banners keep waving smoothly in old worlds.
- `bugfix.render.block_entity_lighting`: fixes lighting on chests, signs and similar blocks.
- `bugfix.render.buffer_growth`: fixes rendering glitches when a lot is on screen.
- `bugfix.render.camera_ray`: the third person camera ignores glass, panes and blocks you can walk through.
- `bugfix.render.child_held_item`: fixes items held by baby mobs.
- `bugfix.render.creeper_charge_depth`: fixes rendering after charged creepers.
- `bugfix.render.delay_close_projectiles`: projectiles you throw don't flash in front of your face.
- `bugfix.render.deleted_vertex_buffer`: fixes a crash from uploading to deleted buffers.
- `bugfix.render.entities_outside_world`: entities above or below the world stay visible.
- `bugfix.render.fire_blending`: fixes transparency of fire on entities.
- `bugfix.render.first_person_shaders`: mob spectator shaders only apply in first person.
- `bugfix.render.fluid_gaps`: closes the gaps around water and lava.
- `bugfix.render.front_view_pitch`: name tags face you in front view.
- `bugfix.render.gui_item_glint`: enchantment glint fits the item in menus.
- `bugfix.render.horizon_at_void`: moves the dark horizon down to the void.
- `bugfix.render.item_cull_face`: fixes inside-out held items.
- `bugfix.render.item_model_side_faces`: closes gaps in the edges of held items.
- `bugfix.render.item_model_transparency`: fixes holes in items with nearly transparent pixels.
- `bugfix.render.item_model_uvs`: fixes the alignment of item textures.
- `bugfix.render.lit_particle_rotation`: fixes particles facing the wrong way.
- `bugfix.render.missing_item_model`: invalid items show the missing model instead of crashing.
- `bugfix.render.moving_block_precision`: fixes pistons jittering far from spawn.
- `bugfix.render.night_vision_clamp`: fixes night vision brightness.
- `bugfix.render.projectile_alpha`: fixes transparency of arrows and fishing bobbers.
- `bugfix.render.removed_block_entities`: removed blocks like chests aren't drawn.
- `bugfix.render.riding_first_person_arm`: your first person arm isn't posed as riding.
- `bugfix.render.riding_head_rotation`: fixes the heads of mobs riding other mobs.
- `bugfix.render.skull_blending`: fixes transparency on mob heads.
- `bugfix.render.sky_darkening`: fixes the sky darkening based on the wrong position.
- `bugfix.render.spectator_hat`: spectators show their hat layer correctly.
- `bugfix.render.spider_eyes_depth`: fixes rendering after spider eyes.
- `bugfix.render.stitcher_expansion`: fixes large texture packs running out of atlas space.
- `bugfix.render.stuck_arrow_lighting`: fixes lighting on arrows stuck in entities.
- `bugfix.render.texture_download_timeouts`: stuck skin downloads give up after 10 seconds.
- `bugfix.render.wither_charge_depth`: fixes rendering after charged withers.
- `bugfix.render.wither_shadow_scale`: wither shadows match their size.

### Resource packs

- `bugfix.resourcepacks.lenient_metadata`: packs with slightly invalid metadata still load.

### Scoreboards

- `bugfix.scoreboard.duplicate_team`: fixes errors from servers adding a team twice.
- `bugfix.scoreboard.null_checks`: fixes errors from servers removing missing teams.
- `bugfix.scoreboard.stale_team_member`: fixes errors from servers removing players from the wrong team.

### Sound

- `bugfix.sound.pause_resume`: pausing and resuming no longer restarts sounds that had stopped.
- `bugfix.sound.silence_unknown`: less log spam from unknown sounds.

### World

- `bugfix.world.cactus_outline_precision`: fixes the cactus outline far from spawn.
- `bugfix.world.height_map_minimum`: fixes lighting data on chunks from servers.
- `bugfix.world.light_update_range`: fixes lighting glitches at the edge of loaded chunks.
- `bugfix.world.village_chunk_loading`: villages stop loading chunks by accident.

</details>

<details>
<summary><b>Fewer allocations</b>: less stutter from garbage collection</summary>

- `mem.alloc.active_chunk_reuse`: reuses the list of active chunks while you stay in one chunk.
- `mem.alloc.armor_texture_cache`: remembers armor textures.
- `mem.alloc.block_pos_offsets`: cheaper block position math.
- `mem.alloc.direction_lookups`: cheaper direction math.
- `mem.alloc.direction_plane_arrays`: reuses lists of directions.
- `mem.alloc.entity_cull_box`: cheaper checks for whether an entity is on screen.
- `mem.alloc.entity_display_name_cache`: remembers entity names for a tick.
- `mem.alloc.entity_light_cache`: remembers entity lighting for a tick.
- `mem.alloc.game_rule_unchanged_write`: skips setting a game rule to the value it already has.
- `mem.alloc.item_name_cache`: remembers item names.
- `mem.alloc.name_tag_text`: cheaper name tags.
- `mem.alloc.nbt_string_cache`: remembers text versions of NBT strings.
- `mem.alloc.owner_uuid_cache`: remembers the owners of tamed mobs.
- `mem.alloc.particle_remove_set`: cheaper removal of dead particles.
- `mem.alloc.reuse_light_positions`: cheaper lighting checks.
- `mem.alloc.singleplayer_hover_events`: skips hover text on names on servers.
- `mem.alloc.skip_default_name`: skips name lookups for most mobs.
- `mem.alloc.text_formatting_lowercase`: cheaper text drawing.
- `mem.alloc.texture_upload_buffer`: texture uploads use a buffer of the right size.

</details>

<details>
<summary><b>Lower memory usage</b></summary>

- `mem.footprint.block_state_transitions`: smaller block state data.
- `mem.footprint.canonical_identifiers`: identical resource names share memory.
- `mem.footprint.compact_light_data`: fully dark or fully lit chunk sections share memory.
- `mem.footprint.compact_sine_table`: a smaller sine table.
- `mem.footprint.mixin_metadata`: frees Mixin's data once the game has started.

</details>

<details>
<summary><b>Memory leak fixes</b></summary>

- `mem.leak.chunk_stream`: closes chunk files after reading.
- `mem.leak.custom_payload_buffers`: frees mod and plugin packets after handling them.
- `mem.leak.enchantment_iterators`: enchantment code no longer holds on to entities.
- `mem.leak.favicon_buffer`: frees the server icon after encoding it.
- `mem.leak.forget_deleted_textures`: deleted textures are fully forgotten.
- `mem.leak.map_renderer_textures`: frees map textures when changing worlds.
- `mem.leak.pathfinding_world`: mob pathfinding no longer holds on to worlds.
- `mem.leak.redstone_torch_toggles`: redstone torches no longer hold on to worlds.
- `mem.leak.texture_download_callback`: frees download callbacks once a skin arrives.
- `mem.leak.uploaded_texture_image`: frees skin images once they're on the GPU.

</details>

<details>
<summary><b>Performance</b></summary>

- `opt.chunk_indexed_ticks`: faster chunk saving.
- `opt.client_chunk_cache`: faster chunk lookups.
- `opt.client_chunk_tick_budget`: chunk updates stay within a time budget each tick.
- `opt.entity_push_query`: faster entity pushing.
- `opt.fast_entity_unload`: faster entity unloading.
- `opt.fast_random`: faster random numbers.
- `opt.inline_chunk_get_block`: faster block lookups.
- `opt.long_map_hash`: faster chunk maps.
- `opt.resourcepacks.path_concat`: faster resource lookups.
- `opt.resourcepacks.reload_index`: faster resource pack reloads with many packs.
- `opt.resourcepacks.trust_asset_index`: faster vanilla asset lookups.
- `opt.skip_client_area_check`: faster entity ticking on the client.
- `opt.skip_world_transition_gc`: no forced pause when changing worlds.
- `opt.synced_data_array`: faster entity data lookups.
- `opt.synced_data_no_lock`: removes needless locking on entity data.
- `opt.textures.shared_download_executor`: skins download on a few shared threads.
- `opt.vec3i_hash`: faster block position maps.
- `opt.world_collisions`: faster collision checks.
- `opt.worldgen.proto_chunk_states`: faster chunk generation.
- `opt.worldgen.terrain_prefetch`: generates terrain ahead of time on other threads.
- `opt.worldgen.vein_visited_set`: faster ore generation.

</details>

<details>
<summary><b>Tweaks</b></summary>

- `tweaks.async_server_ping`: pings the entire server list in parallel, speeding up lookups.
- `tweaks.disable_realms`: lets you hide the Realms button. Has a setting.
- `tweaks.disable_twitch`: removes the broken Twitch streaming integration.
- `tweaks.skip_terrain_screen`: skips the downloading terrain screen.
- `tweaks.skip_world_swap_progress`: skips the loading message when changing worlds.

</details>
