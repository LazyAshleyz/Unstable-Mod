# Unstable

Unstable adds three custom fishing-rod-style items:

- **Nuke Shot** (`unstable:nuke_shot`)
- **Stab Shot** (`unstable:stab_shot`)
- **Flamewrd Rod** (`unstable:flamewrd_rod`)

Each rod has four durability points, does not lose durability when cast, and is consumed when reeled in. Nuke Shot and Stab Shot have no additional weapon effect yet. Reeling in the Flamewrd Rod consumes it and attempts to teleport the exact account `Flamewrd` to a safe spot behind the user. If that account is unavailable, it tries the exact account `_______________` instead. It never teleports the user as a substitute.

Operators with the gamemaster command permission can give rods with:

```text
/orbital Nuke give <playername> <amount>
/orbital Stab give <playername> <amount>
/orbital Flamewrd give <playername> <amount>
```

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
