# Generate Sample RSM JER/XER Messages

1. Use the erlang compiler (instructions in parent [readme](../../../README.md))
2. Using the erlang CLI generate a sample RSM:
```erlang
{ok, Rsm} = asn1ct:value('RoadSafetyMessage', 'RoadSafetyMessage').
file:write_file("../examples/rsm.src", io_lib:format("~p.~n", [Rsm])).
```
3. Edit the output `rsm.src` file to replace the entire Regional extension array with `asn1_NOVALUE`

Before:
```erlang
    ...
    {'Position3D',-608167188,1535545271,59374, 
        [{'RegionalExtension',27,<<"\n\topen_type">>},
         {'RegionalExtension',194,<<"\n\topen_type">>},
         {'RegionalExtension',98,<<"\n\topen_type">>},
         {'RegionalExtension',112,<<"\n\topen_type">>}]
    },
    {'PositionalAccuracy',22,126,19119},
    4045,10156,
    ...
```

After:
```erlang
    ...
    {'Position3D',-608167188,1535545271,59374,asn1_NOVALUE},
    {'PositionalAccuracy',22,126,19119},
    4045,10156,
    ...
```
4. Using the erlang CLI load in the modified `rsm.src` file and output an UPER encoded message:

```erlang
{ok, [RsmReload]} = file:consult("../examples/rsm.src").
{ok, RsmUper} = 'RoadSafetyMessage':encode('RoadSafetyMessage', RsmReload).
file:write_file("../examples/rsm.uper", RsmUper).
```

5. Transfer the `rsm.uper` file into this directory.

7. In a WSL/linux terminal, run the [`./convert-debug`](../../../converter-all) script like this:

```bash
./converter-debug -p RoadSafetyMessage -iuper -oxer examples/2024/rsm/rsm_01.uper > examples/2024/rsm/rsm_01.xml
```

8. Manually update the output `rsm.xml` file to use a valid `id` (no spaces) field:

Before:
```xml
<RoadSafetyMessage>
    <basicType><anANIMAL/></basicType>
    <secMark>59922</secMark>
    <msgCnt>48</msgCnt>
    <id>4F 43 54 45</id>
    ...
```

After:
```xml
<RoadSafetyMessage>
    <basicType><anANIMAL/></basicType>
    <secMark>59922</secMark>
    <msgCnt>48</msgCnt>
    <id>4F435445</id>
    ...
```