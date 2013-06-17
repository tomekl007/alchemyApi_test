package com.orchestr8.android.test;


import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.orchestr8.android.api.AlchemyAPI;
import com.orchestr8.android.api.AlchemyAPI_NamedEntityParams;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AlchemyAPI_Android_TestApp extends Activity {
	public EditText edittext;
	public TextView textview;
	/****
	 * 
	 * Put your API Key into the variable below.  Can get key from http://www.alchemyapi.com/api/register.html
	 */
	public String AlchemyAPI_Key = "d7db4e835b005f9bc465b75d6dd779f0228cbd03";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        edittext = (EditText) findViewById(R.id.entry);
        textview = (TextView) findViewById(R.id.TextView01);
        String url = "http://www.cnn.com/2010/WORLD/meast/12/07/egypt.shark.attack/index.html?hpt=Sbin";
        edittext.setText("be at Busstop at 8:00. Do not forget about dad.");
        edittext.setSingleLine(true);
        textview.setText("");

        textview.setMovementMethod(new ScrollingMovementMethod()); 
        
        
        final Button button = (Button) findViewById(R.id.concept_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SendAlchemyCall("concept");
            }
        });
        
        final Button entity_button = (Button) findViewById(R.id.entity_button);
        entity_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SendAlchemyCall("entity");
            }
        });
        
        final Button keyword_button = (Button) findViewById(R.id.keyword_button);
        keyword_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SendAlchemyCall("keyword");
            }
        });
        
        final Button text_button = (Button) findViewById(R.id.text_button);
        text_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SendAlchemyCall("text");
            }
        });
        
        final Button sentiment_button = (Button) findViewById(R.id.sentiment_button);
        sentiment_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SendAlchemyCall("sentiment");
            }
        });

    }
    
    private void SendAlchemyCall(String call)
    {
    	Document doc = null;
    	AlchemyAPI api = null;
    	try
    	{
    		api = AlchemyAPI.GetInstanceFromString(AlchemyAPI_Key);
    	}
    	catch( IllegalArgumentException ex )
    	{
    		textview.setText("Error loading AlchemyAPI.  Check that you have a valid AlchemyAPI key set in the AlchemyAPI_Key variable.  Keys available at alchemyapi.com.");
    		return;
    	}

    	String someString = edittext.getText().toString();
    	try{
    		if( "concept".equals(call) )
    		{
                Log.d("click","concept");
                doc = api.TextGetRankedConcepts(someString);
                Log.d("doc",doc.toString());
    			ShowDocInTextView(doc, false);
    		}
    		else if( "entity".equals(call))
    		{
    			doc = api.TextGetRankedNamedEntities(someString);
    			ShowDocInTextView(doc, false);
    		}
    		else if( "keyword".equals(call))
    		{
    			doc = api.TextGetRankedKeywords(someString);
    			ShowDocInTextView(doc, false);
    		}
    		else if( "text".equals(call))
    		{
    			doc = api.TextGetTextSentiment(someString);
    			ShowDocInTextView(doc, false);
    		}
    		else if( "sentiment".equals(call))
    		{
    			AlchemyAPI_NamedEntityParams nep = new AlchemyAPI_NamedEntityParams();
    			nep.setSentiment(true);
                doc = api.TextGetRankedNamedEntities(someString,nep);
    			//doc = api.URLGetRankedNamedEntities(someString, nep);
    			ShowDocInTextView(doc, true);
    		}



    		
    	}
    	catch(Throwable t)
    	{
    		textview.setText("Error: " + t.getMessage());
    	}
    }
    
    private void ShowDocInTextView(Document doc, boolean showSentiment)
    {
    	textview.setText("");
    	if( doc == null )
    		return;
    	Element root = doc.getDocumentElement();
        NodeList items = root.getElementsByTagName("text");
        if( showSentiment )
        {
        	NodeList sentiments = root.getElementsByTagName("sentiment");
	        for (int i=0;i<items.getLength();i++){
	        	Node concept = items.item(i);
	        	String astring = concept.getNodeValue();
	        	astring = concept.getChildNodes().item(0).getNodeValue(); 
	        	textview.append("\n" + astring);
	        	if( i < sentiments.getLength() )
	        	{
	        		Node sentiment = sentiments.item(i);
	        		Node aNode = sentiment.getChildNodes().item(1);
	        		Node bNode = aNode.getChildNodes().item(0);
	        		textview.append(" (" + bNode.getNodeValue()+")");
	        	}
	        }       	
        }
        else
        {
	        for (int i=0;i<items.getLength();i++){
	        	Node concept = items.item(i);
	        	String astring = concept.getNodeValue();
	        	astring = concept.getChildNodes().item(0).getNodeValue(); 
	        	textview.append("\n" + astring);
	        }
        }
    }

}
