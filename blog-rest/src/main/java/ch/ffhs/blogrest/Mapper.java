package ch.ffhs.blogrest;

public interface Mapper<SourceType, TargetType> {
    TargetType map(SourceType source);
}
