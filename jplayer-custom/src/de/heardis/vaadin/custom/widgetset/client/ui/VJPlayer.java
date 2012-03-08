package de.heardis.vaadin.custom.widgetset.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VJPlayer extends Widget implements Paintable, ClickHandler {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-jplayer";

	public static final String CLICK_EVENT_IDENTIFIER = "click";

	private static long idCounter = 0;
	
	/** The client side widget identifier */
	protected String paintableId;

	/** Reference to the server connection object. */
	protected ApplicationConnection client;
	
	private JSONObject options;

	private DivElement playerDivElement;
	
	private String mediaUrl;
	
	private String trackTitle;

	private DivElement skinDivElement;

	private float volume;
	
	private JavaScriptObject jPlayerFunction;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VJPlayer() {
		final DivElement div = Document.get().createDivElement();
		setElement(div);
		setStylePrimaryName(CLASSNAME);
		
//		// Tell GWT we are interested in receiving click events
//		sinkEvents(Event.ONCLICK);
//		// Add a handler for the click events (this is similar to FocusWidget.addClickHandler())
//		addDomHandler(this, ClickEvent.getType());
	}

	private DivElement createThemeDiv(final String divId) {
		final DivElement skinDivElement = Document.get().createDivElement();
		skinDivElement.setId(divId);
		skinDivElement.setClassName("jp-audio");
		skinDivElement.setInnerHTML("<div class=\"jp-type-single\">\r\n" + 
				"      <div class=\"jp-gui jp-interface\">\r\n" + 
				"        <ul class=\"jp-controls\">\r\n" + 
				"          <li><a href=\"javascript:;\" class=\"jp-play\" tabindex=\"1\">play</a></li>\r\n" + 
				"          <li><a href=\"javascript:;\" class=\"jp-pause\" tabindex=\"1\">pause</a></li>\r\n" + 
				"          <li><a href=\"javascript:;\" class=\"jp-stop\" tabindex=\"1\">stop</a></li>\r\n" + 
				"          <li><a href=\"javascript:;\" class=\"jp-mute\" tabindex=\"1\" title=\"mute\">mute</a></li>\r\n" + 
				"          <li><a href=\"javascript:;\" class=\"jp-unmute\" tabindex=\"1\" title=\"unmute\">unmute</a></li>\r\n" + 
				"          <li><a href=\"javascript:;\" class=\"jp-volume-max\" tabindex=\"1\" title=\"max volume\">max volume</a></li>\r\n" + 
				"        </ul>\r\n" + 
				"        <div class=\"jp-progress\">\r\n" + 
				"          <div class=\"jp-seek-bar\">\r\n" + 
				"            <div class=\"jp-play-bar\"></div>\r\n" + 
				"          </div>\r\n" + 
				"        </div>\r\n" + 
				"        <div class=\"jp-volume-bar\">\r\n" + 
				"          <div class=\"jp-volume-bar-value\"></div>\r\n" + 
				"        </div>\r\n" + 
				"        <div class=\"jp-time-holder\">\r\n" + 
				"          <div class=\"jp-current-time\"></div>\r\n" + 
				"          <div class=\"jp-duration\"></div>\r\n" + 
				"          <ul class=\"jp-toggles\">\r\n" + 
				"            <li><a href=\"javascript:;\" class=\"jp-repeat\" tabindex=\"1\" title=\"repeat\">repeat</a></li>\r\n" + 
				"            <li><a href=\"javascript:;\" class=\"jp-repeat-off\" tabindex=\"1\" title=\"repeat off\">repeat off</a></li>\r\n" + 
				"          </ul>\r\n" + 
				"        </div>\r\n" + 
				"      </div>\r\n" + 
				"      <div class=\"jp-title\">\r\n" + 
				"        <ul>\r\n" + 
				"          <li>Bubble</li>\r\n" + 
				"        </ul>\r\n" + 
				"      </div>\r\n" + 
				"      <div class=\"jp-no-solution\">\r\n" + 
				"        <span>Update Required</span>\r\n" + 
				"        To play the media you will need to either update your browser to a recent version or update your <a href=\"http://get.adobe.com/flashplayer/\" target=\"_blank\">Flash plugin</a>.\r\n" + 
				"      </div>\r\n" + 
				"    </div>");
		return skinDivElement;
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
	}
	
	public void setMedia(final String url) {
		JavaScriptObject javaScriptObject = null;
		if (url != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mp3", new JSONString(url));
			javaScriptObject = jsonObject.getJavaScriptObject();
			setMediaJS(getPlayerContainerId(), javaScriptObject);
		}
	}
	
	public void setTrackTitle(String trackTitle) {
		setTrackTitleJS(getPlayerThemeContainerId(), trackTitle);
	}

	public void setVolume(float volume) {
		setVolumeJS(getPlayerContainerId(), this.volume);
	}
	
	public native void play() /*-{
		var jPlayer = this.@de.heardis.vaadin.custom.widgetset.client.ui.VJPlayer::jPlayerFunction;
		jPlayer("play")
	}-*/;

	/**
	 * <div class="jp-type-single">
      <div class="jp-gui jp-interface">
        <ul class="jp-controls">
          <li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>
          <li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>
          <li><a href="javascript:;" class="jp-stop" tabindex="1">stop</a></li>
          <li><a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a></li>
          <li><a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a></li>
          <li><a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a></li>
        </ul>
        <div class="jp-progress">
          <div class="jp-seek-bar">
            <div class="jp-play-bar"></div>
          </div>
        </div>
        <div class="jp-volume-bar">
          <div class="jp-volume-bar-value"></div>
        </div>
        <div class="jp-time-holder">
          <div class="jp-current-time"></div>
          <div class="jp-duration"></div>
          <ul class="jp-toggles">
            <li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>
            <li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>
          </ul>
        </div>
      </div>
      <div class="jp-title">
        <ul>
          <li>Bubble</li>
        </ul>
      </div>
      <div class="jp-no-solution">
        <span>Update Required</span>
        To play the media you will need to either update your browser to a recent version or update your <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.
      </div>
    </div>
	 * 
	 * @param jPlayer
	 * @param id
	 * @param options
	 */
	private native void createJPlayer(VJPlayer jPlayer, String id, String mediaUrl, JavaScriptObject options) /*-{
		if (mediaUrl != null) {
			options.ready = function() {
	            jPlayer.@de.heardis.vaadin.custom.widgetset.client.ui.VJPlayer::setMedia(Ljava/lang/String;)(mediaUrl);
	        };
		}
		
		options.volumechange = function(event) {
			jPlayer.@de.heardis.vaadin.custom.widgetset.client.ui.VJPlayer::onVolumeChange(F)(event.jPlayer.options.volume);
		};
        
        var jPlayerContainerIdSelector = $wnd.$("#" + id);
        jPlayerContainerIdSelector.jPlayer(options);
        var jPlayerFunction = jPlayerContainerIdSelector.jPlayer.bind(jPlayerContainerIdSelector);
        this.@de.heardis.vaadin.custom.widgetset.client.ui.VJPlayer::jPlayerFunction = jPlayerFunction;
	}-*/;

    private native void setMediaJS(final String id, final JavaScriptObject mediaOptions) /*-{
    	$wnd.$("#" + id).jPlayer("setMedia", mediaOptions);
	}-*/;
    
    private native void setTrackTitleJS(final String id, final String trackTitle) /*-{
		$wnd.$("#" + id + " .jp-title ul li").text(trackTitle);
	}-*/;

    private native void setVolumeJS(final String id, final float volume) /*-{
		$wnd.$("#" + id).jPlayer("volume", volume);
	}-*/;
	
	/**
     * Called whenever an update is received from the server 
     */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		// This call should be made first. 
		// It handles sizes, captions, tooltips, etc. automatically.
		if (client.updateComponent(this, uidl, true)) {
		    // If client.updateComponent returns true there has been no changes and we
		    // do not need to update anything.
			return;
		}

		// Save reference to server connection object to be able to send
		// user interaction later
		this.client = client;

		// Save the client side identifier (paintable id) for the widget
		paintableId = uidl.getId();
		
		if (uidl.hasAttribute("mediaURL")) {
			this.mediaUrl = client.translateVaadinUri(uidl.getStringAttribute("mediaURL"));
		} else {
			this.mediaUrl = null;
		}
		
		if (uidl.hasAttribute("trackTitle")) {
			this.trackTitle = uidl.getStringAttribute("trackTitle");
		} else {
			this.trackTitle = null;
		}
		
		if (uidl.hasAttribute("volume")) {
			this.volume = uidl.getFloatAttribute("volume");
		} else {
			this.volume = 0.8F;
		}
		
		if (this.playerDivElement == null) {
			this.options = createInitalOptions();
			initializeJPlayerDom(this.mediaUrl);
		} else {
			setMedia(mediaUrl);
			setTrackTitle(this.trackTitle);
			setVolume(this.volume);
			if (uidl.hasVariable("play") && uidl.getBooleanVariable("play")) {
				play();
			}
		}
		
		
	}

    private void onPlaybackBegin() {
    	client.updateVariable(paintableId, "playbackBegin", true, false);
    }
    
    private void onVolumeChange(final float newValue) {
    	client.updateVariable(paintableId, "volumeChange", newValue, true);
    }
	
	/**
     * Called when a native click event is fired.
     * 
     * @param event
     *            the {@link ClickEvent} that was fired
     */
     public void onClick(ClickEvent event) {
		// Send a variable change to the server side component so it knows the widget has been clicked
		String button = "left click";
		// The last parameter (immediate) tells that the update should be sent to the server
		// right away
		client.updateVariable(paintableId, CLICK_EVENT_IDENTIFIER, button, true);
	}
     
     private JSONObject createInitalOptions() {
    	 final JSONObject options = new JSONObject();
    	 options.put("solution", new JSONString("html"));
    	 options.put("supplied", new JSONString("mp3"));
    	 options.put("volume", new JSONNumber(this.volume));
//    	 options.put("cssSelectorAncestor", new JSONString("jplayer"));
    	 
    	 return options;
     }
     
     private String getPlayerContainerId() {
    	 return this.playerDivElement.getId();
     }
     
     private String getPlayerThemeContainerId() {
    	 return this.skinDivElement.getId();
     }
     
     private void initializeJPlayerDom(final String mediaUrl) {
    	this.playerDivElement = Document.get().createDivElement();
 		this.playerDivElement.setId(String.valueOf(idCounter++));
 		
 		this.skinDivElement = createThemeDiv("jp_container_1");
 		
 		getElement().appendChild(playerDivElement);
 		getElement().appendChild(skinDivElement);
 		
 		createJPlayer(this, getPlayerContainerId(), mediaUrl, this.options.getJavaScriptObject());
     }

	private void setjPlayerFunction(final JavaScriptObject jPlayerFunction) {
		if (this.jPlayerFunction != null) {
			throw new IllegalStateException();
		}
		this.jPlayerFunction = jPlayerFunction;
	}
	
	private JavaScriptObject getjPlayerFunction() {
		return this.jPlayerFunction;
	}
     
}
