// JavaScript Document
var cityList = new Array();
cityList['������'] = ['����','����','����','����','����','˳��','��ɽ','����','����','����','����','��ͷ��','ʯ��ɽ','��̨','ͨ��'];
cityList['�Ϻ���'] = ['��ɽ��','������','������', '�����','������','������','�ϻ���','�����','¬����'];
cityList['�㶫ʡ'] = ['������','������','������','��ͷ��','�麣��','��ɽ��','��ɽ��','��ݸ��'];
cityList['������'] = ['������', '�޺���', '������', '������', '������', '��ɽ��', '�����ܱ�'];
cityList['������'] = ['������', '�ϰ���', '������', 'ɳƺ����', '��������', '�山��', '��ɿ���', '������'];
cityList['�����'] = ['��ƽ��', '������', '�Ͽ���', '�ӱ���', '�Ӷ���', '������', '������', '������'];
cityList['����ʡ'] = ['�Ͼ���','������','������'];
cityList['�㽭ʡ'] = ['������','������','������'];
cityList['�Ĵ�ʡ'] = ['�Ĵ�ʡ','�ɶ���'];
cityList['����ʡ'] = ['������'];
cityList['����ʡ'] = ['������','������','Ȫ����','������'];
cityList['ɽ��ʡ'] = ['�ൺ��','������','��̨��','������'];
cityList['����ʡ'] = ['����ʡ','�ϲ���'];
cityList['����ʡ'] = ['������','������'];
cityList['����ʡ'] = ['����ʡ','�Ϸ���'];
cityList['�ӱ�ʡ'] = ['������','ʯ��ׯ��'];
cityList['����ʡ'] = ['֣����','������'];
cityList['����ʡ'] = ['�人��','�˲���'];
cityList['����ʡ'] = ['����ʡ','��ɳ��'];
cityList['����ʡ'] = ['����ʡ','������'];
cityList['ɽ��ʡ'] = ['ɽ��ʡ','̫ԭ��'];
cityList['������ʡ'] = ['������ʡ','��������'];
cityList['����'] = ['����'];

function getPre()
{
var selP = document.getElementById("selP");
var selC = document.getElementById("selC");
var op1 = new Option("-��ѡ��ʡ��-",-1);
selP.add(op1);
var op2 = new Option("-��ѡ����-",-1);
selC.add(op2);
for(var p in cityList )
{
var op = new Option(p,p);
selP.add(op);
			 
}
}
function addCity()
{
var selP = document.getElementById("selP");
var selValue= selP.value;
var selC = document.getElementById("selC"); selC.options.length=0;
for(var c in cityList[selValue])
{
var cvalue=cityList[selValue][c];
var op = new Option(cvalue,cvalue);
selC.add(op);
}
}