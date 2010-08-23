class Ratings
  Like = 'Like'
  Dislike = 'Dislike'
  attr_accessor :ratings
  
  def initialize
    @ratings = {}
  end
  
  # add ability to update which 
  #  can swap items from one set to another
  def add(user_id, item_id, rating)
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
      next if user_id == item_id
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
      all_ratings_count = 0
      class_ratings_count = 0
      user_class_ratings = ratings["#{user_id}:#{class_j}"]
      
       # What happens if you have never liked something or never disliked something
      next if user_class_ratings.nil?
      
      score = 1
      item_ratings.each do |item_rating|
        rating_pair = item_rating.split(':')
        rater_id, rating = rating_pair.first, rating_pair.last
        
        # don't consider user as a rater or other raters 
        #  that aren't in the neighborhood as part of similarity match
        next if user_id == rater_id
        next if options[:neighborhood] and not options[:neighborhood].include?(rater_id)
                
        # for the prior calculation
        all_ratings_count += ratings["#{rater_id}:all"].length
        class_ratings_count += (ratings["#{rater_id}:#{class_j}"] || []).length
        
        similar_ratings = (user_ratings & ratings["#{rater_id}:all"])
        matching_ratings = (user_class_ratings & ratings["#{rater_id}:#{rating}"])
                
        next if similar_ratings == false or matching_ratings == false
        score *= matching_ratings.length.to_f / similar_ratings.length.to_f
      end
      
      score *= class_ratings_count.to_f / all_ratings_count.to_f
            
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