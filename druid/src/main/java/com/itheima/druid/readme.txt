《代码题》
1、

以索引服务方式批量摄取以下json数据
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:01.00Z","city":"guangzhou","platform":"pc","click":"1"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T05:01.00Z","city":"beijing","platform":"pc","click":"1"}
{"timestamp":"2018-12-01T01:03.00Z","city":"guangzhou","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:01.00Z","city":"beijing","platform":"mobile","click":"0"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"pc","click":"1"}
{"timestamp":"2018-12-01T03:01.00Z","city":"shanghai","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:01.00Z","city":"guangzhou","platform":"pc","click":"1"}
{"timestamp":"2018-12-01T05:03.00Z","city":"beijing","platform":"mobile","click":"0"}
{"timestamp":"2018-12-01T01:01.00Z","city":"shanghai","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:01.00Z","city":"guangzhou","platform":"pc","click":"1"}
{"timestamp":"2018-12-01T03:03.00Z","city":"beijing","platform":"mobile","click":"0"}
{"timestamp":"2018-12-01T01:01.00Z","city":"shanghai","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T05:01.00Z","city":"beijing","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"mobile","click":"1"}
{"timestamp":"2018-12-01T01:01.00Z","city":"shanghai","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:01.00Z","city":"guangzhou","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"mobile","click":"0"}
{"timestamp":"2018-12-01T01:01.00Z","city":"shanghai","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"pc","click":"1"}
{"timestamp":"2018-12-01T01:01.00Z","city":"guangzhou","platform":"pc","click":"0"}
{"timestamp":"2018-12-01T01:03.00Z","city":"beijing","platform":"mobile","click":"0"}
{"timestamp":"2018-12-02T01:01.00Z","city":"shanghai","platform":"pc","click":"0"}
{"timestamp":"2018-12-02T02:03.00Z","city":"beijing","platform":"pc","click":"0"}

《代码题》
2、

以MR任务方式摄取以下数据
{"time":"2015-09-12T00:47:00.496Z","channel":"#ca.wikipedia","cityName":null,"comment":"Robot inserta {{Commonscat}} que enllaça amb [[commons:category:Rallicula]]","countryIsoCode":null,"countryName":null,"isAnonymous":false,"isMinor":true,"isNew":false,"isRobot":true,"isUnpatrolled":false,"metroCode":null,"namespace":"Main","page":"Rallicula","regionIsoCode":null,"regionName":null,"user":"PereBot","delta":17,"added":17,"deleted":0}
{"time":"2015-09-12T00:47:05.474Z","channel":"#en.wikipedia","cityName":"Auburn","comment":"/* Status of peremptory norms under international law */ fixed spelling of 'Wimbledon'","countryIsoCode":"AU","countryName":"Australia","isAnonymous":true,"isMinor":false,"isNew":false,"isRobot":false,"isUnpatrolled":false,"metroCode":null,"namespace":"Main","page":"Peremptory norm","regionIsoCode":"NSW","regionName":"New South Wales","user":"60.225.66.142","delta":0,"added":0,"deleted":0}
{"time":"2015-09-12T00:47:08.770Z","channel":"#vi.wikipedia","cityName":null,"comment":"fix L?i CS1: ngày tháng","countryIsoCode":null,"countryName":null,"isAnonymous":false,"isMinor":true,"isNew":false,"isRobot":true,"isUnpatrolled":false,"metroCode":null,"namespace":"Main","page":"Apamea abruzzorum","regionIsoCode":null,"regionName":null,"user":"Cheers!-bot","delta":18,"added":18,"deleted":0}
{"time":"2015-09-12T00:47:11.862Z","channel":"#vi.wikipedia","cityName":null,"comment":"clean up using [[Project:AWB|AWB]]","countryIsoCode":null,"countryName":null,"isAnonymous":false,"isMinor":false,"isNew":false,"isRobot":true,"isUnpatrolled":false,"metroCode":null,"namespace":"Main","page":"Atractus flammigerus","regionIsoCode":null,"regionName":null,"user":"ThitxongkhoiAWB","delta":18,"added":18,"deleted":0}
{"time":"2015-09-12T00:47:13.987Z","channel":"#vi.wikipedia","cityName":null,"comment":"clean up using [[Project:AWB|AWB]]","countryIsoCode":null,"countryName":null,"isAnonymous":false,"isMinor":false,"isNew":false,"isRobot":true,"isUnpatrolled":false,"metroCode":null,"namespace":"Main","page":"Agama mossambica","regionIsoCode":null,"regionName":null,"user":"ThitxongkhoiAWB","delta":18,"added":18,"deleted":0}
{"time":"2015-09-12T00:47:17.009Z","channel":"#ca.wikipedia","cityName":null,"comment":"/* Imperi Austrohongarès */","countryIsoCode":null,"countryName":null,"isAnonymous":false,"isMinor":false,"isNew":false,"isRobot":false,"isUnpatrolled":false,"metroCode":null,"namespace":"Main","page":"Campanya dels Balcans (1914-1918)","regionIsoCode":null,"regionName":null,"user":"Jaumellecha","delta":-20,"added":0,"deleted":20}