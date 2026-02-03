# This is how I will be designing the core of the entire game engine

This time I think it shall be a better idea to make a layout to plan how the engine would work instead lfo just going at it

## Loading phases

The loading stages are set up in specific order since preceding loading stages will require the loaded resources from the previous stage 

- Core setup (Sets up the base of the engine, preparing for the OS being used and starting up the loading screen)
- PreInit (Load all resources, shaders, registries)
- Init (Load all content, blocks, items)