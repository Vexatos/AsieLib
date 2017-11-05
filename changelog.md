==== 0.4.9 ====

  * CHANGED: Increased precision of sound positions.

==== 0.4.8 ====

  * ADDED: Position-independent sound.
  * CHANGED: Split GuiBase and ContainerBase into two classes.

==== 0.4.7 ====

  * FIXED: Added parameter to specify sound rolloff in audio player.

==== 0.4.6 ====

  * FIXED: Made audio system a lot more efficient.
  * FIXED: Version number detection.
  * FIXED: IC2 energy integration.
  * REMOVED: GregTech debugger support for asielib blocks. AsieLib now works with both GregTech 5 and GregTech 6.
  * MISC: Improved colorizing recipes.

==== 0.4.5 ====

  * ADDED: Spy mode and /spy command for server administrators using the chat-radius config option.
  * FIXED: IC2 energy integration not working very well.
  * MISC: Some changes to the audio system.
  * MISC: Allow soft dependencies on specific mod versions.

==== 0.4.4 ====

  * FIXED: Some strange visual bug with blocks.

==== 0.4.3 ====

  * FIXED: Server-side crash related to ray tracing
  * FIXED: Players not being able to place blocks against other blocks (for instance Computronics Colorful Lamps) without sneaking when they should.

==== 0.4.2 ====

  * ADDED: Blueprint support for the BuildCraft Builder.

==== 0.4.1 ====

  * FIXED: Links in Chat not working when Chat Tweaks are enabled.

==== 0.4.0 ====

  * ADDED: Config option to make the /realname command require Op rights on a server
  * ADDED: Support for OpenComputers, AppliedEnergistics and Mekanism wrenches
  * ADDED: Config option to log chat to the server console. Enabled by default.
  * CHANGED: Changed how tools from other mods are handled. This might break addons not updated to this version!
  * FIXED: server chat not being logged to console at all. Now works provided the config option is enabled.

==== 0.3.13 ====

  * FIXED: Fixed license file to represent actual license

==== 0.3.12 ====

  * FIXED: Actually 'fixed' crashes with GregTech 6

==== 0.3.11 ====

  * FIXED: Chat Events not being processed properly
  * FIXED: Properly added a soft dependency on GregTech 5 until GregTech 6 is considered stable
  * MISC: Refactored some code

==== 0.3.10 ====

  * FIXED: Some more problems with the chat tweaks

==== 0.3.9 ====

  * FIXED: /nick command should work properly now!
  * CHANGED: Now you can use /nick to change your own nickname without being Op on the server
    - This can be changed to the previous behaviour in the config
  * CHANGED: Re-added /realname command
  * CHANGED: /me now supports nicknames

==== 0.3.8 ====

  * CHANGED: **AsieLib is not a coremod anymore!**
  * FIXED: Overcomplicated Redstone behaviour
  * FIXED: Weird issues with NBT tags that were causing problems

==== 0.3.7 ====

  * Removed the Shinonome Laboratories.

==== 0.3.6 ====

  * Fixed: Several bugs regarding server-to-client synchronization

==== 0.3.5 ====

  * Added: Ender IO tool support.
  * Misc: Cleanups, tweaks, etc.

==== 0.3.4 ====

  * Misc: We don't bundle the CoFH API anymore. It will load it if it detects it in any other mod, though.

==== 0.3.3 ====

  * Added: Updated CoFH API.
  * Removed: MJ API.
  * Improved: Block Rotation.
  * Enableable: Bane of Arthropods ⑨. If you enable the (EXPERIMENTAL) config option, combine a BoA V weapon with a stack of fermented spider eyes and 37 levels of XP in an Anvil, you get to attack spiders by //looking at them//. How cool is that? (The max range is 10 blocks)

==== 0.3.2 ====

  * Forgot to release this one. Fixes Cipher Blocks, among other things.

==== 0.3.1 ====

  * Fixes a pretty serious bug that broke everything.

==== 0.3.0 ====

  * Things. Many important Tile Entity things, for instance.

==== 0.2.12 ====

  * FIXED: A serious bug present only in 0.2.11 that I won't tell you about. (Thanks Techokami!)

==== 0.2.11 ====

  * FIXED: A serious network-related memory leak. (Thanks Kobata!)
  * **WARNING:** This version has a serious bug in it - please do not use!

==== 0.2.10 ====

  * FIXED: Crash *with* EiraIRC installed (in contrast with crashing *without* which was present in 0.2.9)

==== 0.2.9 ====

  * FIXED: EiraIRC is no longer a dependency for Chat Tweaks. Not the first time I made this mistake.
  * FIXED: mcmod.info now shows correct stuff. For how long?

==== 0.2.8 ====

  * NEW: Anvil tweak! Change colors of item names with dyes in an anvil! (thanks [[http://www.minecraftforum.net/forums/minecraft-discussion/suggestions/2172947-anvils-change-item-label-color-with-dyes|to these guys]])
  * NEW: Proper EiraIRC integration for Chat Tweaks.
  * CHANGED: Chat Tweaks are now off by default.
  * FIXED: Some future-proofing of the networking system for all of my mods.
  * FIXED: Removed some old junk code, shaved a few KBs off the filesize.

==== 0.2.7 ====

  * Fix some issues with disabling chat events. Maybe. If this doesn't fix it, they're Not My Fault(tm).
  * Fix config generation.
  * Add some particle utility functions.

==== 0.2.6 ====

  * Fixes a bug related to the Shinonome part of AsieLib. Only affected 1.7.10.

==== 0.2.5 ====

  * Fixed the config file not being saved. If you have issue with chat plugins, try disabling chatTweaks! (The ChatTweaks are essentially EssentialsChat, the Forge mod)

==== 0.2.4 ====

  * [Techokami/asie] Fix ConcurrentModificationException with the audio library
  * [asie] Make streaming sound sources dependent on the jukebox volume, not the music volume

==== 0.2.4 ====

  * [gamax92] Made BuildCraft and CC APIs optional.
  * [gamax92] Ported to Gradle.
  * [asie] Minor fixes to ICommand to make it compile on Gradle.
  * [asie] Added deobfuscated builds for 0.2.3-1.7.2 and 0.2.4-1.6.4.

==== 0.2.3 ====

  * [Techokami] Ported to 1.7.2.

==== 0.2.3 ====

  * Added: Functions required by Computronics 0.4.0.
  * Added: German (Vexatos) and Polish translations!
  * Added: The ability to change the shout prefix.

==== 0.2.2 ====

  * Fixed: Config file generation.

==== 0.2.1 ====

  * Added: setSampleRate() to streaming playback management, for setSpeed() in Computronics 0.3.1.

==== 0.2.0 ====

  * WARNING: Breaks compatibility with Computronics <= 0.2.4!
  * Added: Far better texture rotation handling.
  * Added: A lot of features which will be used by my mods.

==== 0.1.11 ====

  * (1.7.2) Remove BuildCraft API from the JAR, using Optionals instead. Might help with removing potential crashes in the future.
  * Fixed: BuildCraft wrenches actually take damage now.
  * Fixed: A Computronics crash bug because of some kind of race condition.

==== 0.1.10 ====

  * (1.7.2) Fixed: A server-side crash bug.

==== 0.1.9 ====

  * (1.7.2) Fixed: The coremod now works and appears to work properly.
  * (1.7.2) Fixed: A crash bug in Herobrine's skin rendering.

==== 0.1.8 ====

  * (1.7.2) Fixed: The AccessTransformer works properly now.

==== 0.1.7 ====

  * **Ported to Minecraft 1.7.2.**
==== 0.1.6 ====

  * Fixed: Something a talking cat with a red scarf scolded me about.

==== 0.1.5 ====

  * Changed: Unbundled from Computronics.
  * Fixed: Made the library function on the server side.
  * Fixed: Generalize audio streaming functions to use in non-DFPWM code.
