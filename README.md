# block-replace-mc1.7.10
## Motivation
Some mods contains useless ores and other worlgen blocks. In case if you forgot to disable those blocks or mod doesn't allow to do so, this mod exist to replace blocks at chunk load time.
## Configuration
Add blocks you want to replace in config file 'blockreplace.cfg' in such format:
```
    S:replaceMap <
    modid:blocknametoreplace=modid:blocknamereplacement
    ...
     >
```
For example:
```
    S:replaceMap <
    minecraft:dirt=minecraft:stone
    minecraft:grass=minecraft:stone
    Magneticraft:thorium_ore=IC2:blockOreCopper
    minecraft:red_flower:3=minecraft:red_flower
     >
```
