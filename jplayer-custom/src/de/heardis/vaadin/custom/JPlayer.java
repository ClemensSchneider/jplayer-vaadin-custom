package de.heardis.vaadin.custom;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * Server side component for the VJPlayer widget.
 */
@com.vaadin.ui.ClientWidget(de.heardis.vaadin.custom.widgetset.client.ui.VJPlayer.class)
public class JPlayer extends AbstractComponent {

	private String mediaURL;
	
	private String trackTitle;
	
	private float volume = 0.8F;

	private boolean play;
	
	public String getMediaURL() {
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
		requestRepaint();
	}
	
	public void setTrackTitle(String trackTitle) {
		this.trackTitle = trackTitle;
		requestRepaint();
	}
	
	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		requestRepaint();
	}
	
	public void play() {
		if (this.play) {
			return;
		}
		this.play = true;
		requestRepaint();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);

		if (this.mediaURL != null) {
			target.addAttribute("mediaURL", this.mediaURL);
		}
		if (this.trackTitle != null) {
			target.addAttribute("trackTitle", this.trackTitle);
		}
		target.addAttribute("volume", this.volume);
		target.addVariable(this, "play", this.play);
	}

	/**
	 * Receive and handle events and other variable changes from the client.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		super.changeVariables(source, variables);

		// Variables set by the widget are returned in the "variables" map.

		if (variables.containsKey("playbackBegin")) {
			firePlaybackBeginEvent();
//			requestRepaint();
		}
		if (variables.containsKey("volumeChange")) {
			fireEvent(new VolumeChangeEvent(this, (Float) variables.get("volumeChange")));
		}
	}
	
	public void addListener(final VolumeChangeListener volumeChangeListener) {
		addListener(VolumeChangeEvent.class, volumeChangeListener, VolumeChangeListener.volumeChangeMethod);
	}
	
	private void firePlaybackBeginEvent() {
		System.out.println("Fired PlaybackBegin!");
	}
	
	public interface VolumeChangeListener extends Serializable {
		public static final Method volumeChangeMethod = ReflectTools.findMethod(
                VolumeChangeListener.class, "volumeChange", VolumeChangeEvent.class);
		
		public void volumeChange(VolumeChangeEvent event);
	}
	
	public class VolumeChangeEvent extends Component.Event {

		private static final long serialVersionUID = 1L;
		
		private float newVolume;
		
		public VolumeChangeEvent(Component source, float newVolume) {
			super(source);
			this.newVolume = newVolume;
		}

		public float getVolume() {
			return newVolume;
		}
	}

}
