package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

public class GenericModifyListener implements ModifyListener
{
	private RepositoryEditorPageModelEntities _editorPage ;

	private int _valueId ;
	
	public GenericModifyListener(RepositoryEditorPageModelEntities editorPage, int id ) {
		_editorPage = editorPage;
		_valueId = id ;
	}

	protected RepositoryEditorPageModelEntities getEditorPage()
	{
		return _editorPage ;
	}
	
	public void modifyText(ModifyEvent e) 
	{
		if ( _editorPage.isPopulateInProgress() )
		{
			_editorPage.log(this, "Text modified : populate in progress => no action" );
		}
		else
		{
			Text text = (Text) e.widget ;
			String sTextValue = text.getText();

    		//--- Update the model if the value is not the same	        		
			//String sOldValue = getValue() ;
			String sOldValue = _editorPage.getModelValue(_valueId );
			_editorPage.log(this, "Text modified : '" + sOldValue + "' --> '" + sTextValue + "'" );
			if ( ! sOldValue.equals(sTextValue) ) // Has it realy changed ?
			{
				_editorPage.log(this, "Text modified : diferent values => update the model" );

				//--- Update the model 
				//setValue(sTextValue);
				_editorPage.setModelValue(_valueId, sTextValue );
				
				//--- The model has been modified ( "*" in the Eclipse editor's tab )
				_editorPage.setDirty();
			}
			else
			{
				_editorPage.log(this, "Text modified : same value => model not updated" );
			}
		}
	}

}
