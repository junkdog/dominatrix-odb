package net.onedaybeard.dominatrix.demo.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.onedaybeard.dominatrix.artemis.JsonId;

import com.artemis.Component;

@Data @EqualsAndHashCode(callSuper=false) @Accessors(fluent=true) @NoArgsConstructor @AllArgsConstructor
public final class JsonKey extends Component implements JsonId
{
	private String name;
}
