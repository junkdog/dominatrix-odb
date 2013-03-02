package net.onedaybeard.dominatrix.demo.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

@Data @EqualsAndHashCode(callSuper=false) @Accessors(fluent=true)
public class Tint extends Component
{
	private Color color = Color.WHITE;
}
