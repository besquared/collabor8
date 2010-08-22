class Ratings
  Like = 'Like'
  Dislike = 'Dislike'
  attr_accessor :ratings
  
  def initialize
    @ratings = {}
  end
  
  def add(user_id, item_id, rating)
    ratings["count:all"] ||= 0
    ratings["count:all"] += 1
    
    ratings["count:#{rating}"] ||= 0
    ratings["count:#{rating}"] += 1
    
    ratings["#{item_id}"] ||= Set.new
    ratings["#{item_id}"] << "#{user_id}:#{rating}"
    
    ratings["#{user_id}:all"] ||= Set.new
    ratings["#{user_id}:all"] << item_id
    
    ratings["#{user_id}:#{rating}"] ||= Set.new
    ratings["#{user_id}:#{rating}"] << item_id
  end
  
  def suggest(user_id, item_ids, options = {})
    suggestions = {}
    item_ids.each do |item_id|
      suggestions[item_id] = classify(user_id, item_id, options)
    end
    suggestions
  end

  def classify(user_id, item_id, options = {})
    top_class = nil
    top_score = 0.0
    
    item_ratings = ratings["#{item_id}"]
    user_ratings = ratings["#{user_id}:all"]
    [Like, Dislike].each do |class_j|
      user_class_ratings = ratings["#{user_id}:#{class_j}"]
      
      score = 1
      item_ratings.each do |item_rating|
        rating_pair = item_rating.split(':')
        rater_id, rating = rating_pair.first, rating_pair.last
        
        # don't consider raters that aren't in the neighborhood to be considered
        next if options[:neighborhood] and not options[:neighborhood].include?(rater_id)
        
        # for the prior calculation
        # all_ratings += ratings["#{rater_id}:all"].length
        # class_ratings += ratings["#{rater_id}:#{rating}"].length
        
        similar_ratings = (user_ratings & ratings["#{rater_id}:all"])
        matching_ratings = (user_class_ratings & ratings["#{rater_id}:#{rating}"])
        
        next if similar_ratings == false or matching_ratings == false
        score *= matching_ratings.length.to_f / similar_ratings.length.to_f
      end
      
      # this only works well when you don't consider constrained neighborhoods
      #  we should be collecting statistics for the prior as we scan through the sets
      score *= ratings["count:#{class_j}"].to_f / ratings["count:all"].to_f
      
      if top_class == nil
        top_score = score
        top_class = class_j
      elsif top_score < score
        top_score = score
        top_class = class_j
      end
    end
    
    top_class
  end
end