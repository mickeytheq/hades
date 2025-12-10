# Introduction

Hades is a plugin for [Strange Eons](https://cgjennings.ca/eons/). It allows users to create custom content for Arkham Horror: LCG.

Hades is inspired by and uses some elements of the [AHLCG plugin](https://github.com/tokeeto/StrangeEonsAHLCG/) created by Pilute23/JaqenZann and maintained by tokeeto. It is intended to be very familiar to users of the existing plugin with some key changes.

I decided to explore this project due to some frustrations with the existing AHLCG plugin and having too much free time!

I am also the author of [Zoop](https://mickeytheq.github.io/ZoopDocs/) a plugin built on top of the existing AHLCG plugin.

**Hades is a work in progress so this manual currently represents a set of planned features**

# Features

Features in _italic_ are either not yet available or partially complete. Click through the respective links for more information

- Performance - Hades performance is approximately 5-10 times faster than the existing AHLCG plugin. See [performance](feature/performance.md) for more details
- High-resolution templates - Produces high quality images
- Card combinations - Hades supports (almost) any combination of card front and backs of the available [card faces](feature/cardfaces.md). See [creating a new card](ui/newcard.md)
- Arno Pro enforced - The preferred font for AHLCG cards is Arno Pro which is required. See [font setup](ui/fontsetup.md)
- _Project settings_ - A centralised place to specify [project configuration](ui/projectconfiguration.md) such as encounter sets and collections instead of the current options of global and per-card
- _Comprehensive card types_ - The card types have been simplified. For example Story Assets are just an Asset. More information on [card faces](feature/cardfaces.md)
- _Migration from the existing AHLCG plugin_ - Easy [migration](migration/ahlcgplugin.md) of projects built using the existing plugin to Hades
- _Feature parity with the existing AHLCG plugin_ - This will naturally be delivered as support for all card types and providing a migration logic
- _Supported by Zoop_ - Allowing the existing key functions of Zoop such as TTS export, print and play and arkham.build

# Thanks to

- Tokeeto for maintaining the AHLCG plugin
- Pilute23/JaqenZann for creating the AHLCG plugin
- Chris Jennings for creating Strange Eons
- Fantasy Flight for creating Arkham Horror: LCG
