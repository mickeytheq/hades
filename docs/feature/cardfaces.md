# About card faces

A Hades card is comprised of one or two faces. The majority of cards have two faces, even if the back is a generic face such as a player or encounter card back.

Card faces implementations are designed to be independent of each other (with some exceptions below). When creating a new card you can either

- select from standard default options, e.g. an 'Asset' default option would create a card with an Asset front and Player Back back
- choose custom faces, e.g. you could choose an 'Asset' front face with a 'Enemy' back face

This allows almost any combination of front and back faces to be created easily.

Hades supports the following card faces. Some are different to the AHLCG plugin as Hades has merged some card types together for simplicity.

The status columns indicates the current development state

- $${\color{black}Ready}$$ - ready for real use
- $${\color{green}Beta}$$ - complete and in final testing/feedback cycle
- $${\color{blue}Alpha}$$ - built but needs review testing
- $${\color{red}Missing}$$ - not yet built

| Hades card type | Restrictions | AHLCG plugin card types | Status |
| ------ | -------- | --------- | ---- |
| Asset | None | Asset, Asset/Asset, Story Asset | $${\color{blue}Alpha}$$ |
| Event | None | Event | <span style="color:blue">Alpha</span> |
| Skill | None |Skill | <span style="color:blue">Alpha</span> |
| Customizable Upgrade | None | Customizable Upgrades | <span style="color:red">Missing</span> |
| Investigator | None | Investiator, Story investigator | <span style="color:blue">Alpha</span> |
| Investigator Back | Only supported as the back face paired with an Investigator front face | (back of investigator card) | <span style="color:blue">Alpha</span> |
| Minicard | None | Mini Investigator Marker | Missing |
| Scenario | None | Scenario | Missing |
| Agenda | None | Agenda, numerous variants | Missing |
| Agenda Back | Only supported as the back face paired with an Agenda front face | (back of agenda card) | Missing |
| Act | None | Act, numerous variants | Missing |
| Act Back | Only supported as the back face paired with an Act front face | (back of act card) | Missing |
| Location | None (however 'copy front' options are only available with a front location | Location, Location/Location | Missing |
| Treachery | None | Treachery, Treachery (weakness) | <span style="color:blue">Alpha</span> |
| Enemy | None | Enemy, Enemy (weakness) | Missing |
| Treachery Location | None | TreacheryLocation | Missing |
| Story | None | Story, numerous variants | Missing |
| Key | None | Key | Missing |
| Portrait | None | Potrait | Missing |
| Concealed | None | Concealed | Missing |
| Rules | None | Campaign Rules | Missing |
| Player card back | None | (the default back on many cards) | <span style="color:blue">Alpha</span> |
| Encounter card back | None | (the default back on many cards) | <span style="color:blue">Alpha</span> |
| Purple player card back | None | (the default back on customizable upgrade cards) | Missing |
| Concealed card back | None | (the default back on customizable upgrade cards) | Missing |
