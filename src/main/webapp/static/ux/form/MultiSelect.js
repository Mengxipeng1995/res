Ext.define('Ext.ux.MultiComboBox', {
    extend: 'Ext.form.ComboBox',
    alias: 'widget.multicombobox',
    xtype: 'multicombobox',
    initComponent: function(){
    	this.multiSelect = true;
    	this.listConfig = {
	    	  itemTpl : Ext.create('Ext.XTemplate','<input type="checkbox"  value="{id}" />  {name}'),
	  	      onItemSelect: function(record) {    
	  	    	  var node = this.getNode(record);
    	          if (node) {
    	             Ext.fly(node).addCls(this.selectedItemCls);
    	             
    	             var checkboxs = node.getElementsByTagName("input");
    	             if(checkboxs!=null)
    	             {
    	            	 var checkbox = checkboxs[0];
  	    				 checkbox.checked = true;
    	             }
    	          }
	  	      },
	  	      listeners:{
  	    		  itemclick:function(view, record, item, index, e, eOpts ){
  	    			  var isSelected = view.isSelected(item);
  	    			  var checkboxs = item.getElementsByTagName("input");
  	    			  if(checkboxs!=null)
  	    			  {
  	    				  var checkbox = checkboxs[0];
  	    				  if(!isSelected)
  	    				  {
  	    					  checkbox.checked = true;
  	    				  }else{
  	    					  checkbox.checked = false;
  	    				  }
  	    			  }
  	    		  }
	  	      }    	  
	  	}   	
    	this.callParent();
    },
    setupItems: function() {
        var me = this;
        
        me.boundList = Ext.create('Ext.view.BoundList', Ext.apply({
            deferInitialRefresh: false,
            border: false,
            multiSelect: true,
            store: me.store,
            displayField: me.displayField,
            disabled: me.disabled
        }, me.listConfig));
        
        me.boundList.getSelectionModel().on('selectionchange', me.onSelectChange, me);
        return {
            border: true,
            layout: 'fit',
            title: me.title,
            tbar: me.tbar,
            items: me.boundList
        };
    }
});