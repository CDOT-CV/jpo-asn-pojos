# Generate Sample PSM JER/XER Messages

1. Use the erlang compiler (instructions in parent [readme](../../../README.md))
2. Using the erlang CLI generate a sample PSM:
```erlang
{ok, Psm} = asn1ct:value('PersonalSafetyMessage', 'PersonalSafetyMessage').
file:write_file("../examples/psm.src", io_lib:format("~p.~n", [Psm])).
```
3. Edit the output `psm.src` file to replace the entire Regional extension array with `asn1_NOVALUE`

Before:
```erlang
{'PersonalSafetyMessage',anANIMAL,59922,48,<<"OCTE">>,
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
{'PersonalSafetyMessage',anANIMAL,59922,48,<<"OCTE">>,
    {'Position3D',-608167188,1535545271,59374,asn1_NOVALUE},
    {'PositionalAccuracy',22,126,19119},
    4045,10156,
    ...
```
4. Using the erlang CLI load in the modified `psm.src` file and output an UPER encoded message:

```erlang
{ok, [PsmReload]} = file:consult("../examples/psm.src").
{ok, PsmUper} = 'PersonalSafetyMessage':encode('PersonalSafetyMessage', PsmReload).
file:write_file("../examples/psm.uper", PsmUper).
```

5. Transfer the `psm.uper` file into this directory.
6. Manually edit the bit string fields (any field that has a "value" and "length" key):

Before:
```json
{
  ...
  "activitySubType": {
    "value": "02",
    "length": 7
  },
  "assistType": {
    "value": "20",
    "length": 3
  },
  "sizing": {
    "value": "10",
    "length": 4
  },
  ...
}
```

After:
```json
{
  ...
  "activitySubType": "02",
  "assistType": "20",
  "sizing": "10",
  ...
}
```

7. In a WSL/linux terminal, run the [`./convert-all`](../../../converter-all) script like this:

```bash
./converter-all -p PersonalSafetyMessage -ijer -oxer examples/2024/psm/psm_animal.json > examples/2024/psm/psm_animal.xml
```

8. Manually update the output `psm.xml` file to use a valid `id` (no spaces) field:

Before:
```xml
<PersonalSafetyMessage>
    <basicType><anANIMAL/></basicType>
    <secMark>59922</secMark>
    <msgCnt>48</msgCnt>
    <id>4F 43 54 45</id>
    ...
```

After:
```xml
<PersonalSafetyMessage>
    <basicType><anANIMAL/></basicType>
    <secMark>59922</secMark>
    <msgCnt>48</msgCnt>
    <id>4F435445</id>
    ...
```