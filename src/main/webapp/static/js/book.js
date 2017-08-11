//图书表格数据
var bookGridStore = Ext.create("Ext.data.Store", {

    fields: ['bookid', 'title', 'keywords', 'pageNums', 'publisherName', 'publisherAddress', 'pubDateStr'],
    proxy: {
        type: 'ajax',
        url: 'bcm/outline',
        reader: {
            root: 'content',
            totalProperty: 'totalElements'
        },
        simpleSortMode: true
    }

});

//图书分类树数据
var bookCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
    autoLoad: false,
    proxy: {
        type: 'ajax',
        url: 'bookCategroy/getSons'
    },
    root: {
        nodeType: 'async',
        expanded: true,
        text: '图书分类',
        id: '1'
    },
    fields: ['id', 'text'],
    nodeParam: 'id',
    listeners: {
        load: function (store, records, successful, eOpts) {
            if (successful) {
                Ext.Array.each(records, function (record, index, countriesItSelf) {
                    record.data.text = record.data.name;
                    record.commit();
                });


            }
        }

    }
});


var updateBookCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
    autoLoad: false,
    proxy: {
        type: 'ajax',
        url: 'bookCategroy/getGategoryWithBooid'
    },
    root: {
        nodeType: 'async',
        expanded: true,
        name: '图书分类',
        flag: true,
        id: '1'
    },
    fields: ['id', 'text'],
    nodeParam: 'id',
    listeners: {
        load: function (store, records, successful, eOpts) {
            Ext.getCmp('bookEntryFormCategroy').expandAll();
        }

    }
});


var updateBookCategoryPanel = Ext.create('Ext.panel.Panel', {
    width: '85%',
    autoWidth: true,
    region: 'center',
    items: [
        {
            xtype: 'form',
            width: 350,
            id: "bookCategoryMagageForm",
            style: 'margin:auto',
            buttonAlign: 'center',
//	/margin  : '15 0 0 0',
            items: [
                {
                    xtype: 'fieldset',
                    id: 'bookCategoryMagageTitle',
                    title: '新增分类信息',
                    collapsible: false,
                    width: 350,
                    items: [
                        {
                            xtype: "textfield",
                            fieldLabel: '分类名称<font color="red">*</font>',
                            id: 'bookCategoryMagageFormName',
                            name: 'name',
                            allowBlank: false,
                            blankText: '分类名称为必填项'
                        },
                        {
                            xtype: "textfield",
                            fieldLabel: '分类Id<font color="red">*</font>',
                            id: 'bookCategoryMagageFormId',
                            hidden: true,
                            name: 'id',
                            blankText: '分类Id为必填项'

                        },
                        {
                            xtype: "textfield",
                            id: 'bookCategoryMagageFormResourceType',
                            hidden: true,
                            value: '1',
                            name: 'resourcesType'

                        }
                        , {
                            xtype: "textfield",
                            fieldLabel: '父分类Id<font color="red">*</font>',
                            id: 'bookCategoryMagageFormParentId',
                            hidden: true,
                            name: 'pid',
                            allowBlank: false,
                            blankText: '分类Id为必填项'

                        }


                    ]
                }
            ],
            buttons: [
                {
                    text: '保存',
                    listeners: {
                        click: function () {
                            var form = Ext.getCmp("bookCategoryMagageForm");
                            if (form.isValid()) {
                                form.submit({
                                    url: $ctx + '/bookCategroy/save',
                                    success: function (data) {
                                        updateBookCategoryPanel.findParentByType("window").close();
                                        Ext.Msg.alert('提示', data);
                                        bookCategoryTreeStore.proxy.extraParams = {};
                                        bookCategoryTreeStore.load();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示', action.result.msg);
                                    }
                                });
                            } else {
                                Ext.Msg.alert('提示', '表单信息有误,请核查');
                            }
                        }
                    }

                }, {
                    text: '关闭',
                    listeners: {
                        click: function () {
                            updateBookCategoryPanel.findParentByType("window").close();
                        }
                    }

                }
            ]

        }]
});

//图书分类树
var bookCategoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '图书管理',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store: bookCategoryTreeStore,
    rootVisible: true,
    listeners: {
        'itemclick': function (view, record, item, index, e, eOpts) {
            bookGridStore.proxy.extraParams = {cid: record.id};
            bookGridStore.loadPage(1);
        },
        'afteritemexpand': function (node) {
            //console.info(node);
        },
        'load': function (store, records, successful, operation, node, eOpts) {
            if (successful && node.id == '1' && records.length > 0) {


                if (successful && node.id == '1' && records.length > 0) {

                    bookCategoryWestPanel.getSelectionModel().select(records[0]);
                    bookCategoryWestPanel.fireEvent('itemclick', bookCategoryWestPanel, records[0]);
                    bookCategoryWestPanel.fireEvent('expand', bookCategoryWestPanel, records[0]);
                    this.getRootNode().getChildAt(0).expand();

                }

            }

        },
        'itemcontextmenu': function (view, record, item, index, e, eOpts) {
            if (Ext.getCmp('rightClickMenu') != null) {
                Ext.destroy(Ext.getCmp('rightClickMenu'));
            }
            e.preventDefault();
            e.stopEvent();
            var nodemenu = new Ext.menu.Menu({
                floating: true,
                id: 'rightClickMenu',
                align: 'center',
                items: [
                    {
                        text: '修改',
                        icon: $ctx + '/static/images/btn/edit.gif',
                        handler: function () {

                            var form = Ext.getCmp("bookCategoryMagageForm");
                            form.reset();
                            Ext.getCmp('bookCategoryMagageTitle').setTitle('修改分类信息');
                            var id = record.data.id; //节点id
                            Ext.getCmp('bookCategoryMagageFormId').setValue(id);
                            Ext.getCmp('bookCategoryMagageFormName').setValue(record.data.name);
                            Ext.getCmp('bookCategoryMagageFormParentId').setValue(record.parentNode.data.id);


                            Ext.create('Ext.window.Window',
                                {
                                    modal: true,
                                    height: 320,
                                    width: 380,
                                    region: 'center',
                                    autoWidth: true,
                                    layout: "border",
                                    items: [updateBookCategoryPanel],
                                    listeners: {
                                        'close': function (win) {
                                            win.removeAll(false);
                                        }
                                    }

                                }
                            ).show();
                        }
                    },
                    {
                        text: '新增',
                        icon: $ctx + '/static/images/btn/add_0906.png',
                        handler: function () {
                            var form = Ext.getCmp("bookCategoryMagageForm");
                            form.reset();
                            Ext.getCmp('bookCategoryMagageTitle').setTitle('新增分类信息');
                            var id = record.data.id; //节点id
                            Ext.getCmp('bookCategoryMagageFormParentId').setValue(id);

                            Ext.create('Ext.window.Window',
                                {
                                    modal: true,
                                    height: 320,
                                    width: 380,
                                    region: 'center',
                                    autoWidth: true,
                                    layout: "border",
                                    items: [updateBookCategoryPanel],
                                    listeners: {
                                        'close': function (win) {
                                            win.removeAll(false);
                                        }
                                    }

                                }
                            ).show();
                        }
                    },
                    {
                        text: '删除',
                        icon: $ctx + '/static/images/btn/delete_0906.png',
                        handler: function () {

                            var tipstr = "是否删除？";
                            Ext.Msg.confirm("提示：", tipstr, function (btn) {
                                if (btn == "yes") {
                                    var id = record.data.id; //节点id
                                    Ext.Ajax.request({
                                        url: $ctx + "/bookCategroy/delete",
                                        params: {"id": id},
                                        disableCaching: false,
                                        success: function (resp, opts) {
                                            Ext.Msg.alert("提示：", "删除成功！");
                                            bookCategoryTreeStore.proxy.extraParams = {};
                                            bookCategoryTreeStore.load();

                                        },
                                        failure: function (resp, opts) {
                                            Ext.Msg.alert("提示：", "删除失败,其重试");
                                        }
                                    });
                                }
                            });
                        }
                    }, {
                        text: '关闭',
                        icon: $ctx + '/static/images/btn/close_0906.png',
                        handler: function () {
                            Ext.destroy(Ext.getCmp('rightClickMenu'));
                        }
                    }
                ]

            });
            nodemenu.showAt(e.getXY());

        }
    }
});

//图书实体面板
var updateBookEntryPanel = Ext.create('Ext.panel.Panel', {
    width: '85%',
    autoWidth: true,
    region: 'center',
    scrollable: true,
    listeners: {
        added: function () {
        }
    },
    items: [
        {
            xtype: 'form',
            width: 700,
            id: "bookEntryForm",
            style: 'margin:auto;width:500px;',
            buttonAlign: 'center',
//	/margin  : '15 0 0 0',
            items: [
                {
                    xtype: 'fieldset',
                    id: 'bookEntryTitle',
                    title: '新增图书信息',
                    collapsible: false,
                    width: 700,
                    items: [
                        {
                            xtype: "textfield",
                            hidden: true,
                            name: 'id'
                        },
                        {
                            xtype: "textfield",
                            fieldLabel: '图书名称<font color="red">*</font>',
                            name: 'title',
                            allowBlank: false,
                            blankText: '专题名称为必填项',
                            width: 650,
                            height: 30,
                        },
                        {
                            xtype: "textfield",
                            fieldLabel: '关键词',
                            name: 'keySentences'
                        },
                        {
                            xtype: "numberfield",
                            allowDecimals:false,
                            allowNegative: false,
                            allowBlank: false,
                            blankText: '页数为必填项',
                            minValue:1,
                            fieldLabel: '页数',
                            name: 'pageNums'
                        },
                        {
                            xtype: "textfield",
                            allowBlank: false,
                            blankText: 'ISBN为必填项',
                            fieldLabel: 'ISBN',
                            name: 'publisherName'
                        },
                        {
                            xtype: "textfield",
                            fieldLabel: '出版单位',
                            name: 'publisherAddress'
                        },
                        {
                            xtype: "textfield",
                            fieldLabel: '版次',
                            name: 'revision'
                        }, {
                            xtype: "textfield",
                            fieldLabel: 'CIP号',
                            name: 'cip'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            maxValue:2147483647,
                            allowBlank: false,
                            allowDecimals: false, // 允许小数点
                            allowNegative: false, // 允许负数
                            hideTrigger: true,
                            keyNavEnabled: true,
                            mouseWheelEnabled: true,
                            fieldLabel: '版权登记号',
                            name: 'copyrightNum'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '版权信息',
                            name: 'copyrightInfo'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '版印次',
                            name: 'printEdition'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '编辑推荐',
                            name: 'editorialRecommendation'
                        }, {
                            xtype: "datefield",
                            format: "Y-m-d",
                            fieldLabel: '出版年月',
                            name: 'publishYm'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '出版国别',
                            name: 'publisherCountry'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '丛书名',
                            name: 'seriesName'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            allowBlank: false,
                            fieldLabel: '定价',
                            name: 'price'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '读者对象',
                            name: 'readerObject'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '分类',
                            name: 'category'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '分类号',
                            name: 'categoryNum'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '分社',
                            name: 'branch'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '广告语',
                            name: 'advertising'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '后记',
                            name: 'postscript'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            maxValue:2147483647,
                            allowBlank: false,
                            allowDecimals: false, // 允许小数点
                            allowNegative: false, // 允许负数
                            fieldLabel: '厚',
                            name: 'thick'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '获奖情况',
                            name: 'awardSituation'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '建议上架类别',
                            name: 'recommendedShelfCategories'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '介子',
                            name: 'medium'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '开本',
                            name: 'folio'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '媒体评论',
                            name: 'mediaReview'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '名人推荐',
                            name: 'celebrityRecommendation'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '目录',
                            name: 'catalog'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '内容简介',
                            name: 'contentValidity'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '前言',
                            name: 'preface'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '商品类型',
                            name: 'commodityType'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '条码书号',
                            name: 'barcode'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '图书尺寸',
                            name: 'size'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '图书等级',
                            name: 'level'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '图书品牌',
                            name: 'brand'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '图书状态',
                            name: 'status'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '序言',
                            name: 'preface2'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '业务分类',
                            name: 'serviceClassification'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '业务分类名称',
                            name: 'serviceClassificationName'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '译者',
                            name: 'translator'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '译者简介',
                            name: 'translatorProfiles'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '译者序',
                            name: 'translatorPreface'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            maxValue:2147483647,
                            allowBlank: false,
                            allowDecimals: true, // 允许小数点
                            allowNegative: false, // 允许负数
                            fieldLabel: '印章',
                            name: 'sheet'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '营销分类',
                            name: 'marketingClassification'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '用纸',
                            name: 'paper'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '语种',
                            name: 'languages'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '在线试读',
                            name: 'readOnline'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '责任编辑',
                            name: 'editorCharge'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '中图法分类',
                            name: 'graphClassification'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            maxValue:2147483647,
                            allowBlank: false,
                            allowDecimals: false, // 允许小数点
                            allowNegative: false, // 允许负数
                            fieldLabel: '重量',
                            name: 'weight'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '主题词',
                            name: 'keywords'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '装帧',
                            name: 'binding'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            maxValue:2147483647,
                            allowBlank: false,
                            allowDecimals: false, // 允许小数点
                            allowNegative: false,
                            fieldLabel: '字数',
                            name: 'wordcount'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '作者',
                            name: 'author'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '作者简介',
                            name: 'authorBrief'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '作者序',
                            name: 'authorPreface'
                        }, {
                            xtype: "datefield",
                            format: "Y-m-d",
                            fieldLabel: '上架日期',
                            name: 'shelvesDate'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '分类code',
                            name: 'categoryCode'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '上架建议',
                            name: 'shelfSuggestions'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            maxValue:2147483647,
                            allowBlank: false,
                            allowDecimals: false, // 允许小数点
                            allowNegative: false,
                            fieldLabel: '章节数',
                            name: 'chapterCount'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '原书名',
                            name: 'originalTitle'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '原出版社',
                            name: 'original_publisher'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            maxValue:2147483647,
                            allowBlank: false,
                            allowDecimals: false, // 允许小数点
                            allowNegative: false,
                            fieldLabel: '有无CD',
                            name: 'cdCount'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            maxValue:2147483647,
                            allowBlank: false,
                            allowDecimals: false, // 允许小数点
                            allowNegative: false,
                            fieldLabel: '有无磁盘',
                            name: 'diskCount'
                        }, {
                            xtype: "numberfield",
                            minValue:1,
                            maxValue:2147483647,
                            allowBlank: false,
                            allowDecimals: false, // 允许小数点
                            allowNegative: false,
                            fieldLabel: '有无磁带',
                            name: 'magneticCount'
                        }, {
                            xtype: "textfield",
                            fieldLabel: 'makrk数据',
                            name: 'markData'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '作者国籍',
                            name: 'author_nationality'
                        }, {
                            xtype: "textfield",
                            fieldLabel: '出版社推荐',
                            name: 'pressRecommendation'
                        }, {
                            xtype: "textfield",
                            hidden: true,
                            name: 'cid'

                        }, {
                            xtype: 'treepanel',
                            split: true, //显示分隔条
                            autoScroll: true,
                            rootVisible: true,
                            id: 'bookEntryFormCategroy',
                            store: updateBookCategoryTreeStore,
                            columns: [
                                {
                                    xtype: 'treecolumn', //this is so we know which column will show the tree
                                    text: '分类：',
                                    flex: 2,
                                    sortable: false,
                                    width: "50",
                                    dataIndex: 'name'
                                }, {
                                    xtype: 'checkcolumn',
                                    //  header: '授权',
                                    dataIndex: 'flag',
                                    width: "50%",
                                    renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                                        if (value == true) {
                                            changeAuthorizeItems(record.id, true);
                                        } else {
                                            changeAuthorizeItems(record.id, false);
                                        }
                                        return (new Ext.grid.column.CheckColumn).renderer(value);


                                    },
                                    stopSelection: false,
                                    listeners: {
                                        checkchange: function (item, rowIndex, checked, eOpts) {
                                            var category = updateBookCategoryTreeStore.getAt(rowIndex);
                                            var rootid = updateBookCategoryTreeStore.getRoot().id;
                                            if (checked) {
                                                category.set('flag', true);
                                                if (category.id != rootid)
                                                    cascadeUpdateTree(category, true);
                                            } else {
                                                //根节点必须勾选

                                                if (category.id == rootid) {
                                                    //Ext.Msg.alert('提示', '根节点必须勾选');
                                                    category.set('flag', true);
                                                } else {
                                                    category.set('flag', false);
                                                    cascadeUpdateTree(category, false);
                                                }

                                            }

                                            category.commit();

                                        }
                                    }
                                }
                            ]
                        }


                    ]
                }
            ],
            buttons: [
                {
                    text: '保存',
                    listeners: {
                        click: function () {
                            var form = Ext.getCmp("bookEntryForm");
                            var ids = "";
                            //遍历勾选的节点
                            updateBookCategoryTreeStore.each(function (record) {
                                if (record.data.flag) {
                                    ids += record.id + ";";
                                }
                            });
                            form.getForm().findField('cid').setValue(ids);


                            if (form.isValid()) {
                                form.submit({
                                    url: $ctx + '/book/save',
                                    success: function (form, action) {
                                        updateBookEntryPanel.findParentByType("window").close();
                                        Ext.Msg.alert('提示', '操作成功');
                                        bookGridStore.loadPage(1);
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示', action.result.msg);
                                    }
                                });
                            } else {
                                Ext.Msg.alert('提示', '表单信息有误,请核查');
                            }
                        }
                    }

                }, {
                    text: '关闭',
                    listeners: {
                        click: function () {
                            updateBookEntryPanel.findParentByType("window").close();
                        }
                    }

                }
            ]

        }]
});

//图书查询结果显示
var bookGridPanel = Ext.create('Ext.panel.Panel', {
    title: '图书列表',
    width: '85%',
    height: '100%',
    autoWidth: true,
    region: 'center',
    layout: {
        type: "border"
    },
    autoScroll: true,
    items: [{
        xtype: 'grid',
        layout: 'fit',
        region: 'center',
        height: '100%',
        widht: '100%',
        autoWidth: true,
        columnLines: true,
        store: bookGridStore,
        dockedItems: [
            {
                xtype: 'toolbar',
                //hidden:true,
                enableOverflow: true,
                items: [
                    {
                        xtype: 'textfield',
                        fieldLabel: '检索词',
                        labelWidth: 50,
                        name: 'kw',
                        id: 'bookKW'
                    }, {
                        xtype: 'button',
                        text: '查询',
                        listeners: {
                            'click': function () {

                                var cat, sect = bookCategoryWestPanel.getSelection();

                                if (sect.length > 0) {
                                    cat = sect[0].data.id;
                                }
                                bookGridStore.proxy.extraParams = {
                                    kw: Ext.getCmp("bookKW").getValue(),
                                    cid: cat

                                };
                                bookGridStore.loadPage(1);
                            }
                        }

                    }, {
                        xtype: 'button',
                        text: '创建图书',
                        listeners: {
                            'click': function () {
                                updateBookCategoryTreeStore.proxy.extraParams = {};
                                updateBookCategoryTreeStore.load();

                                var form = Ext.getCmp("bookEntryForm");
                                form.reset();
                                Ext.getCmp('bookEntryTitle').setTitle('新增专题信息');
                                var window = Ext.create('Ext.window.Window',
                                    {
                                        modal: true,
                                        height: 600,
                                        width: 800,
                                        region: 'center',
                                        autoWidth: true,
                                        layout: "border",
                                        items: [],
                                        listeners: {
                                            'close': function (win) {
                                                win.removeAll(false);
                                            }
                                        }

                                    }
                                ).show();

                                window.add(updateBookEntryPanel);

                            }
                        }

                    }
                ]
            }
        ],
        listeners: {
            'itemdblclick': function (view, record, item, index, e, eOpts) {
//					'title','keywords','pageNums','publisherName','publisherAddress','pubDateStr'
            }
        },
        columns: [

            {
                header: "序号",
                xtype: "rownumberer",
                align: 'center',
                hideable: false,
                width: '4%'
            },
            {
                header: "<div style='text-align:center'>标题</div>",
                dataIndex: "title",
                align: 'left',
                width: '21.5%',
                listeners: {
                    click: function (grid, rowIndex, colIndex) {
                        var record = bookGridStore.getAt(colIndex);
                        updateBookCategoryTreeStore.proxy.extraParams = {
                            bookid: record.data.bookid
                        };
                        window.open("newbook/bookdetails?id=" + record.data.bookid);
                        $.ajax({
                            url: "newbook/bookdetails",
                            data: {id: record.data.bookid},
                            async: false,
                            success: function () {

                            }
                        });

                        // alert(record.data.bookid);
                    }
                }

            },
            {
                header: "<div style='text-align:center'>关键字</div>",
                dataIndex: "keywords",
                align: 'left',
                width: '20%'
            },
            {
                header: "页数",
                dataIndex: "pageNums",
                align: 'center',
                width: '7%'
            },
            {
                header: "<div style='text-align:center'>ISBN</div>",
                dataIndex: "publisherAddress",
                align: 'left',
                width: '17%'
            }
            ,
            {
                header: "出版单位",
                dataIndex: "publisherName",
                align: 'center',
                width: '15%'
            },
            {
                header: "版次",
                dataIndex: "pubDateStr",
                align: 'center',
                width: '7%'
            }
            , {
                xtype: 'actioncolumn',
                header: "操作",
                align: 'center',
                width: '8%',
                items: [
                    {
                        icon: $ctx + '/static/images/btn/edit.gif',  // Use a URL in the icon config
                        tooltip: '修改',
                        handler: function (grid, rowIndex, colIndex) {
                            var record = bookGridStore.getAt(rowIndex);

                            updateBookCategoryTreeStore.proxy.extraParams = {
                                bookid: record.data.bookid
                            };
                            $.ajax({
                                url: "newbook/getbooks?bookid=" + record.data.bookid,
                                dataType: "JSON",
                                success: function (data) {
                                    updateBookCategoryTreeStore.load();

                                    var zhmon = null;
                                    var zhdate = null;


                                    var date = new Date(data.shelvesDate);
                                    var zhyear = date.getFullYear();


                                    if ((date.getMonth() + 1) < 10) {
                                        zhmon = "0" + (date.getMonth() + 1);
                                    } else {
                                        zhmon = (date.getMonth() + 1);
                                    }
                                    if ((date.getDate()) < 10) {
                                        zhdate = "0" + (date.getDate())
                                    } else {
                                        zhdate = (date.getDate());
                                    }


                                    var newdate = zhyear + '-' + zhmon + '-' + zhdate;

                                    var form = Ext.getCmp("bookEntryForm");
                                    form.reset();
                                    //   alert(data.shelvesDate);
                                    // alert(data.publish);
                                    form = form.getForm();
                                    form.findField('id').setValue(record.data.bookid);
                                    form.findField('title').setValue(data.title);
                                    form.findField('keySentences').setValue(data.keySentences);
                                    form.findField('pageNums').setValue(data.pageSize);
                                    form.findField('publisherName').setValue(data.isbn);
                                    //版次
                                    form.findField('revision').setValue(data.revision);
                                    //出版社
                                    form.findField('publisherAddress').setValue(data.publish);
                                    //cip
                                    form.findField('cip').setValue(data.cip);
                                    //版权登记号
                                    form.findField('copyrightNum').setValue(data.copyrightNum);
                                    //版权信息
                                    form.findField('copyrightInfo').setValue(data.copyrightInfo);
                                    //版印次
                                    form.findField('printEdition').setValue(data.printEdition);
                                    //编辑推荐
                                    form.findField('editorialRecommendation').setValue(data.editorialRecommendation);
                                    //出版年月
                                    form.findField('publishYm').setValue(data.publishYm);
                                    //出版国别
                                    form.findField('publisherCountry').setValue(data.publisherCountry);
                                    //丛书名
                                    form.findField('seriesName').setValue(data.seriesName);
                                    //定价
                                    form.findField('price').setValue(data.price);
                                    //读者对象
                                    form.findField('readerObject').setValue(data.readerObject);
                                    //分类
                                    form.findField('category').setValue(data.category);
                                    //分类号
                                    form.findField('categoryNum').setValue(data.categoryNum);
                                    //分社
                                    form.findField('branch').setValue(data.branch);
                                    //广告语
                                    form.findField('advertising').setValue(data.advertising);
                                    //后记
                                    form.findField('postscript').setValue(data.postscript);
                                    //厚
                                    form.findField('thick').setValue(data.thick);
                                    //获奖情况
                                    form.findField('awardSituation').setValue(data.awardSituation);
                                    //建议上架类别
                                    form.findField('recommendedShelfCategories').setValue(data.recommendedShelfCategories);
                                    //介子
                                    form.findField('medium').setValue(data.medium);
                                    //开本
                                    form.findField('folio').setValue(data.folio);
                                    //媒体评论
                                    form.findField('mediaReview').setValue(data.mediaReview);
                                    //名人推荐
                                    form.findField('celebrityRecommendation').setValue(data.celebrityRecommendation);
                                    //目录
                                    form.findField('catalog').setValue(data.catalog);
                                    //内容简介
                                    form.findField('contentValidity').setValue(data.contentValidity);
                                    //前言
                                    form.findField('preface').setValue(data.preface);
                                    //商品类型
                                    form.findField('commodityType').setValue(data.commodityType);
                                    //条码书号
                                    form.findField('barcode').setValue(data.barcode);
                                    //图书尺寸
                                    form.findField('size').setValue(data.size);
                                    //图书等级
                                    form.findField('level').setValue(data.level);
                                    //图书品牌
                                    form.findField('brand').setValue(data.brand);
                                    //图书状态
                                    form.findField('status').setValue(data.status);
                                    //序言
                                    form.findField('preface2').setValue(data.preface2);
                                    //业务分类
                                    form.findField('serviceClassification').setValue(data.serviceClassification);
                                    //业务分类名称
                                    form.findField('serviceClassificationName').setValue(data.serviceClassificationName);
                                    //翻译者
                                    form.findField('translator').setValue(data.translator);
                                    //翻译者简介
                                    form.findField('translatorProfiles').setValue(data.translatorProfiles);
                                    //翻译者序
                                    form.findField('translatorPreface').setValue(data.translatorPreface);
                                    //印章
                                    form.findField('sheet').setValue(data.sheet);
                                    //营销分类
                                    form.findField('marketingClassification').setValue(data.marketingClassification);
                                    //用纸
                                    form.findField('paper').setValue(data.paper);
                                    //语种
                                    form.findField('languages').setValue(data.languages);
                                    //在线阅读
                                    form.findField('readOnline').setValue(data.readOnline);
                                    //责任编辑
                                    form.findField('editorCharge').setValue(data.editorCharge);
                                    //中图法分类
                                    form.findField('graphClassification').setValue(data.graphClassification);
                                    //重量
                                    form.findField('weight').setValue(data.weight);
                                    //主题词
                                    form.findField('keywords').setValue(data.keywords);
                                    //装帧
                                    form.findField('binding').setValue(data.binding);
                                    //字数
                                    form.findField('wordcount').setValue(data.wordcount);
                                    //作者
                                    form.findField('author').setValue(data.author);
                                    //作者简介
                                    form.findField('authorBrief').setValue(data.authorBrief);
                                    //作者序
                                    form.findField('authorPreface').setValue(data.authorPreface);
                                    //关键词
                                    form.findField('keySentences').setValue(data.keySentences);
                                    //上架日期
                                    form.findField('shelvesDate').setValue(newdate);
                                    //分类code
                                    form.findField('categoryCode').setValue(data.categoryCode);
                                    //上架建议
                                    form.findField('shelfSuggestions').setValue(data.shelfSuggestions);
                                    //章节数
                                    form.findField('chapterCount').setValue(data.chapterCount);
                                    //原书名
                                    form.findField('originalTitle').setValue(data.originalTitle);
                                    //原出版社
                                    form.findField('original_publisher').setValue(data.original_publisher);
                                    //有无cd
                                    form.findField('cdCount').setValue(data.cdCount);
                                    //有无磁盘
                                    form.findField('diskCount').setValue(data.diskCount);
                                    //有无磁带
                                    form.findField('magneticCount').setValue(data.magneticCount);
                                    //mark数据
                                    form.findField('markData').setValue(data.markData);
                                    //作者国籍
                                    form.findField('author_nationality').setValue(data.author_nationality);
                                    //出版社推荐
                                    form.findField('pressRecommendation').setValue(data.pressRecommendation);
                                    // //上传附件路径
                                    // form.findField('filepath').setValue(data.filepath);


                                    Ext.getCmp('bookEntryTitle').setTitle('修改图书信息');
                                    var window = Ext.create('Ext.window.Window',
                                        {
                                            modal: true,
                                            height: 600,
                                            width: 800,
                                            region: 'center',
                                            autoWidth: true,
                                            layout: "border",
                                            items: [],
                                            listeners: {
                                                'close': function (win) {
                                                    win.removeAll(false);
                                                }
                                            }

                                        }
                                    ).show();

                                    window.add(updateBookEntryPanel);
                                }
                            });


                        }
                    }, {}, {
                        icon: $ctx + '/static/images/btn/delete_0906.png',  // Use a URL in the icon config
                        tooltip: '删除',
                        handler: function (grid, rowIndex, colIndex) {
                            var record = bookGridStore.getAt(rowIndex);
                            $.ajax({
                                url: "book/deletenew",
                                data: {id: record.data.bookid},
                                async: false,
                                dataType: 'json',
                                success: function (data) {
                                    if (data.success) {
                                        bookGridStore.loadPage(1);

                                    } else {
                                        Ext.Msg.alert('提示', data.msg);
                                    }

                                }
                            });
                        }
                    }
                ]

            }
        ]
    }],
    bbar: [
        {
            xtype: 'pagingtoolbar',
            store: bookGridStore,
            displayInfo: true,
            width: '100%',
            height: 30,
            anchorSize: 100,
            displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
            beforePageText: '第',
            afterPageText: '页-共{0}页',
            emptyMsg: "没有内容可以显示",
            nextText: '',
            prevText: '',
            firstText: '',
            lastText: ''
        }
    ]
});


var bookMainPanel = new Ext.Panel({
    height: "85%",
    width: "100%",
    border: 5,
    region: 'center',
    autoWidth: true,
    layout: "border",
    items: [bookCategoryWestPanel, bookGridPanel]
});