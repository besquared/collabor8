class Ratings
  Like = 'Like'
  Dislike = 'Dislike'
  attr_accessor :ub_ratings
  attr_accessor :ib_ratings
  
  def initialize
    @ub_ratings = {}
    @ib_ratings = {}
  end
  
  # add ability to update which 
  #  can swap items from one set to another
  def add(user_id, item_id, rating)
    add_user_based(user_id, item_id, rating)
    add_item_based(user_id, item_id, rating)
  end
  
  def add_user_based(user_id, item_id, rating)
    ub_ratings["#{item_id}"] ||= Set.new
    ub_ratings["#{item_id}"] << "#{user_id}:#{rating}"
    
    ub_ratings["#{user_id}:all"] ||= Set.new
    ub_ratings["#{user_id}:all"] << item_id
    
    ub_ratings["#{user_id}:#{rating}"] ||= Set.new
    ub_ratings["#{user_id}:#{rating}"] << item_id
  end
  
  def add_item_based(user_id, item_id, rating)
    ib_ratings["#{user_id}"] ||= Set.new
    ib_ratings["#{user_id}"] << "#{item_id}:#{rating}"
    
    ib_ratings["#{item_id}:all"] ||= Set.new
    ib_ratings["#{item_id}:all"] << user_id
    
    ib_ratings["#{item_id}:#{rating}"] ||= Set.new
    ib_ratings["#{item_id}:#{rating}"] << user_id
  end
  
  def suggest(user_id, item_ids, options = {})
    suggestions = {}
    item_ids.each do |item_id|
      next if user_id == item_id
      suggestions[item_id] = classify(user_id, item_id, options)
    end
    suggestions
  end
  
  # options should contain
  def classify(user_id, item_id, options = {})
    user_based = user_based_classification(user_id, item_id, options)
    item_based = item_based_classification(user_id, item_id, options)
    
    like_score = user_based[Like] * item_based[Like]
    dislike_score = user_based[Dislike] * item_based[Dislike]

    like_score > dislike_score ? [Ratings::Like, like_score] : [Ratings::Dislike, dislike_score]
  end

  def user_based_classification(user_id, item_id, options = {})
    top_class = nil
    top_score = 0.0
    scores = {}
    item_ratings = ub_ratings["#{item_id}"]
    user_ratings = ub_ratings["#{user_id}:all"]
    [Like, Dislike].each do |class_j|
      scores[class_j] = 1
      all_ratings_count = 0
      class_ratings_count = 0
      user_class_ratings = ub_ratings["#{user_id}:#{class_j}"]
      
       # What happens if you have never liked something or never disliked something
      next if user_class_ratings.nil?
      
      score = 1
      item_ratings.each do |item_rating|
        rating_pair = item_rating.split(':')
        rater_id, rating = rating_pair.first, rating_pair.last
        
        # don't consider user as a rater or other raters 
        #  that aren't in the neighborhood as part of similarity match
        next if user_id == rater_id
        next if options[:user_neighborhood] and not options[:user_neighborhood].include?(rater_id)
                
        # for the prior calculation
        all_ratings_count += ub_ratings["#{rater_id}:all"].length
        class_ratings_count += (ub_ratings["#{rater_id}:#{class_j}"] || []).length
        
        similar_ratings = (user_ratings & ub_ratings["#{rater_id}:all"])
        matching_ratings = (user_class_ratings & ub_ratings["#{rater_id}:#{rating}"])
                
        next if similar_ratings == false or matching_ratings == false
        scores[class_j] *= matching_ratings.length.to_f / similar_ratings.length.to_f
      end
      
      scores[class_j] *= class_ratings_count.to_f / all_ratings_count.to_f
    end
    
    scores
  end
  
  # classify the the user item combo based on item ratings
  #  neighborhood here is a list of items we want to compare
  #  ourselves to, this would be the list of people we follow
  def item_based_classification(user_id, item_id, options = {})
    scores = {}
    user_ratings = ib_ratings["#{user_id}"]
    item_ratings = ib_ratings["#{item_id}:all"]
    [Like, Dislike].each do |class_j|
      scores[class_j] = 1
      all_ratings_count = 0
      class_ratings_count = 0
      item_class_ratings = ib_ratings["#{item_id}:#{class_j}"]
      
       # What happens if an item has never been liked or never been disliked
      next if item_class_ratings.nil?
            
      user_ratings.each do |user_rating|
        rating_pair = user_rating.split(':')
        rated_id, rating = rating_pair.first, rating_pair.last

        # don't consider user as a rater or other raters 
        #  that aren't in the neighborhood as part of similarity match
        next if item_id == rated_id
        next if options[:item_neighborhood] and not options[:item_neighborhood].include?(rated_id)

        # for the prior calculation
        all_ratings_count += ib_ratings["#{rated_id}:all"].length
        class_ratings_count += (ib_ratings["#{rated_id}:#{class_j}"] || []).length

        # remove the user_id from the list of ratings here
        similar_ratings = (item_ratings & (ib_ratings["#{rated_id}:all"] - user_id))
        matching_ratings = (item_class_ratings & (ib_ratings["#{rated_id}:#{rating}"] - user_id))
                
        next if similar_ratings == false or matching_ratings == false
        scores[class_j] *= matching_ratings.length.to_f / similar_ratings.length.to_f
      end

      scores[class_j] *= class_ratings_count.to_f / all_ratings_count.to_f
    end

    scores
  end
end