package tt.calories.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QMeal is a Querydsl query type for Meal
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMeal extends EntityPathBase<Meal> {

    private static final long serialVersionUID = 865373843L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeal meal = new QMeal("meal");

    public final NumberPath<Integer> calories = createNumber("calories", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.sql.Date> mealDate = createDate("mealDate", java.sql.Date.class);

    public final TimePath<java.sql.Time> mealTime = createTime("mealTime", java.sql.Time.class);

    public final QUser user;

    public QMeal(String variable) {
        this(Meal.class, forVariable(variable), INITS);
    }

    public QMeal(Path<? extends Meal> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMeal(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMeal(PathMetadata<?> metadata, PathInits inits) {
        this(Meal.class, metadata, inits);
    }

    public QMeal(Class<? extends Meal> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

