# Soulbound

Soulbound is a mod for Minecraft that adds an enchantment for keeping items upon death. It is built for the 
[Fabric](https://fabricmc.net/) modding toolchain.

## Addons

Out of the box, Soulbound supports [Trinkets](https://www.curseforge.com/minecraft/mc-mods/trinkets-fabric).

If your mod adds an additional player inventory, consider adding integration.

### Maven

```groovy
repositories {
    maven { url "https://maven.tehnut.info" }
}

dependencies {
    modImplementation "info.tehnut.soulbound:Soulbound:${soulbound_version}"
}
```

The API is documented with JavaDocs in 
[`SoulboundContainer`](./src/main/java/info/tehnut/soulbound/api/SoulboundContainer.java).