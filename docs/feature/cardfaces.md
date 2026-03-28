# About card faces

A Hades card is comprised of one or two faces. The majority of cards have two faces, even if the back is a generic face such as a player or encounter card back.

Card faces implementations are designed to be independent of each other (with some exceptions below). When creating a new card you can either

- select from standard default options, e.g. an 'Asset' default option would create a card with an Asset front and Player Back back
- choose custom faces, e.g. you could choose an 'Asset' front face with a 'Enemy' back face

This allows almost any combination of front and back faces to be created easily.

Hades supports the following card faces. Some are different to the AHLCG plugin as Hades has merged some card types together for simplicity.

The status columns indicates the current development state

- Ready - ready for real use
- Beta - complete and in final testing/feedback cycle
- Alpha - built but needs review testing
- Missing - not yet built

PPI available indicates the template resolution that is available

| Hades card type | Restrictions | AHLCG plugin card types | Status | PPI available |
| ------ | -------- | --------- | ---- | ---- |
| Asset | None | Asset, Asset/Asset, Story Asset | Beta | 600 |
| Event | None | Event | Beta | 600, except for multi-class |
| Skill | None |Skill | Beta | 600 for Guardian, Rogue, Survivor, Weakness |
| Customizable Upgrade | None | Customizable Upgrades | Missing | Missing |
| Investigator | None | Investigator, Story investigator | Beta | 300 |
| Investigator Back | Only supported as the back face paired with an Investigator front face | (back of investigator card) | Beta | 300 |
| Minicard | None | Mini Investigator Marker | Beta | 300 |
| Scenario Reference | None | Chaos | Beta | 600 |
| Agenda | None | Agenda, numerous variants | Beta | 600 |
| Agenda Back | Only supported as the back face paired with an Agenda front face | (back of agenda card) | Beta | 600 |
| Act | None | Act, numerous variants | Beta | 600 |
| Act Back | Only supported as the back face paired with an Act front face | (back of act card) | Beta | 600 |
| Location | None (however 'copy front' options are only available with a front location) | Location, Location/Location | Beta | 600 |
| Treachery | None | Treachery, Treachery (weakness) | Beta | 600 |
| Enemy | None | Enemy, Enemy (weakness) | Beta | 600 |
| Treachery Location | None | TreacheryLocation | Missing | Missing |
| Story | None | Story, numerous variants | Missing | Missing |
| Key | None | Key | Missing | Missing |
| Image | None | Potrait | Beta | 600 |
| Concealed | None | Concealed | Missing | Missing |
| Rules | None | Campaign Rules | Missing | Missing |
| Player card back | None | (the default back on many cards) | Beta | 600 |
| Encounter card back | None | (the default back on many cards) | Beta | 600 |
| Purple player card back | None | (the default back on customizable upgrade cards) | Missing | Missing |
| Concealed card back | None | (the default back on customizable upgrade cards) | Missing | Missing |
