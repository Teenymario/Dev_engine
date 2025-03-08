# Learning game engine  
This is a game engine with its only purpose to help me learn about computers and game engines.  
This application will also serve as testing grounds for the extended universe mod. To create aspects of the mod outside of minecraft in a clean environment and then once finished, implement it.  

## Performance  
This game engine will also be used for performance learning practices. Because I will need to know all about optimizing java code for when I make the ####### 1.12.2 optimized mod loader I can identify issues in the game and fix them efficiently

# Development goals
- [x] Create a tick system, allow for no framerate cap
- [x] Create instantiate, pre init, init and post init loading phases
- [x] Create a class to manage the registering, storing and usage of blocks. Create registry names for blocks.
- [x] Create basic resource system (similar to that of minecraft)
- [x] Create texture atlas
- [x] Create new rendering pipeline
- [x] Create a simple 16x16x16 chunk of minecraft blocks
- [x] Create a culled chunk mesher
- [ ] Make a GUI system
- [ ] Chunk rendering optimisations (greedy meshing)
- [ ] Maybe implement full RGB color rendering
- [ ] Introduce prototype terrain generation
- [ ] Terrain gen optimisations
- [ ] Mimic cubic chunks like world
- [ ] Optimised world save format
- [ ] Create a basic loading screen
- [ ] Create a basic ticking system
- [ ] Create a basic modloader for the game using ASM bytecode manipulation
- [ ] Create efficient and optimised modding api that mimics/is similar to that of forge 1.12.2
- [ ] Create a basic mod
- [ ] Use bytecode manipulation from the modding api to manipulate and rewrite code within the game
- [ ] Modify a mod using another mod

### Potential points of consideration:
Making chunk meshes via a growth algorithm, finding the unknown walls of a 3D maze where air is the walls  
Using raytracing to mesh a chunk, maybe implementing it with the previous system as well

### What do we do after the goals?

We tackle the ####### optimised 1.12.2 mod loader