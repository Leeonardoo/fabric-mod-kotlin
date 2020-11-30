### *As the original repo, this one will be removed if Dream requests so too*
# Manhunt Fabric Kotlin
A mod which implements a lot of the mechanics found in Dream's (https://youtube.com/user/DreamTraps) Minecraft Manhunt plugin.

***Contributions are welcome!***

## Important stuff
This mod is *heavily* based on [YTG1234's original Manhunt Fabric mod](https://github.com/YTG1234/manhunt-fabric).
All the credit for the implementation and current features goes to him.
Actually for now, this version only has small modifications in order to work with Kotlin, but I do plan to add new things here too.
Even though, I can't guarantee that this mod will get any updates, bug fixes, new features or any support. If you want a 
more stable and finished version, please take a look at the original mod made by him.

This is my first mod for Minecraft on Kotlin, so for sure there's a lot to improve here. Feel free to make any PR's!

## Extra Features
It's all managed using Minecraft commands.
This mod adds two new commands: `/speedrunner` and `/hunters`.

## How it works:
If you're not familiar with Dream's Minecraft Manhunt series on Youtube, you should check that out.
* You can set the speedrunner by using `/speedrunner set <player>`. That sets that specific Player to be the speedrunner.
* You can view who the speedrunner is by using `/speedrunner get`.
* You can unset the speedrunner by using `/speedrunner clear`.

<br/>

* You can add hunters by using `/hunters add <player>`.
* You can clear the hunter list by using `/hunters clear`.
* You can get the hunter list by using `/hunters get`.

### Differences:
* This mod uses the lodestone compass mechanic introduced in Minecraft 1.16.
* This mod is configurable! Yes! You can view the comments in the config itself to understand it.

This mod also adds a Cloth Config screen you can open using Mod Menu (client-side only, of course).