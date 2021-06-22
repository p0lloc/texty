# texty
Simple library for localizing and sending text from various sources with the help of Adventure and MiniMessage.

This is mainly built for MiniMessage use cases such as config files. If you don't need most of the features described, the Adventure built-in way already works flawlessly.

## Features
- Other sources than resource bundles (such as Bukkit YAML)
- Send messages with prefix, set a default prefix
- Programmatically determine locale for identities

## Custom Texty renderers
- ItemStack - render display name and lore

## Custom MiniMessage renderer
I've also included a custom renderer to render MiniMessage on top of the usual localized rendering.  

This allows you to use ``Texty#translatable`` and let MiniMessage parse components on the go. Use case for this is for example config files where you want to avoid calling Texty methods all the time when you can simply use the built-in Paper methods.

This is super hacky and replaces the renderer inside the GlobalTranslator through reflection. I haven't found any way to store metadata inside components so I had to work this around by identifying custom components by prefixing and delimiting. Use at your own risk!