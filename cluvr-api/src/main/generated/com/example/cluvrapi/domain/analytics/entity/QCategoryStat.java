import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCategoryStat is a Querydsl query type for CategoryStat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategoryStat extends EntityPathBase<CategoryStat> {

    private static final long serialVersionUID = -73885514L;

    public static final QCategoryStat categoryStat = new QCategoryStat("categoryStat");

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> totalAnswer = createNumber("totalAnswer", Integer.class);

    public final NumberPath<Integer> totalQuestion = createNumber("totalQuestion", Integer.class);

    public final NumberPath<Integer> totalScore = createNumber("totalScore", Integer.class);

    public final NumberPath<Integer> totalSelected = createNumber("totalSelected", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCategoryStat(String variable) {
        super(CategoryStat.class, forVariable(variable));
    }

    public QCategoryStat(Path<? extends CategoryStat> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategoryStat(PathMetadata metadata) {
        super(CategoryStat.class, metadata);
    }

}

