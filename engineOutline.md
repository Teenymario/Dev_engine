# This is how I will be designing the core of the entire game engine

This time I think it shall be a better idea to make a layout to plan how the engine would work instead lfo just going at it. The scope of this will change as I create more content and more proper things

## Loading phases for main game engine

The loading stages are set up in specific order since preceding loading stages will require the loaded resources from the previous stage 

- CoreSetup (Sets up the base of the engine, preparing for the OS being used and starting up the loading screen)
- PreInit (Load all resources, shaders, registries)
- Init (Load all content, blocks, items)
- PostInit (Load up all mechanics, UI, world gen, keybinds)
- Finalise (Everything else and open to world/main menu)

## Loading phases for the mod loader

This here is how the mod loader that we make as an experiment will attach into the main game and load it's content. The mod loader will have a scheduling system and specific loading stages in which mods will be more able than forge (this will eventually become the blueprint for the new mod loader for 1.12.2 I have planned in mind). When a mod loads in a specific phase of the game it is "registered" to load during that phase

Note: For those who dont know, IMC is inter mod communication, which is how mods communicate in order to create cross mod compatability

- PreLoad (Mods here will run before the game is even started, as in, before the `public static void main` of the whole game)
- PreLoadASM (Same as before, but isolated specifically for any and all ASM bytecode manipulations)
- CoreSetup (Mods register to load during the game's CoreSetup phase)
- PreInit (Mods register to load during the game's PreInit phase)
- PreInitCompat (After PreInit is done and every mod has loaded it's resources and shaders and registries, etc... IMC, dependancy, ASM)
- Init (Mods register to load during the game's Init phase)
- InitCompat (After mods have now loaded all their blocks and items and content, etc...  IMC, dependancy, ASM)
- PostInit (Mods register to load during the game's PostInit phase)
- PostInitCompat (After mods have now loaded all their mechanics and UI and world gen, keybinds, etc... IMC, dependancy, ASM)
- PreFinalise (Mods register to run during the game's Finalise phase)
- Finalise (After everything has been loaded through the game's finalise phase, mods have another phase afterwards where they can do whatever they like to do. Once this phase has finished we open up to the main menu)

This then leaves us with a total of 10 different loading phases for the game to finish loading and open up for the player to play the game
