package de.dogedev.ld35.ashley;

import com.badlogic.ashley.core.ComponentMapper;
import de.dogedev.ld35.ashley.components.AnimationComponent;
import de.dogedev.ld35.ashley.components.NameComponent;
import de.dogedev.ld35.ashley.components.PositionComponent;
import de.dogedev.ld35.ashley.components.SpriteComponent;

/**
 * Created by Furuha on 12.03.2016.
 */
public class ComponentMappers {

    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<NameComponent> name = ComponentMapper.getFor(NameComponent.class);

}
